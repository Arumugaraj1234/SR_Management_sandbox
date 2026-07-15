package com.vmfg.tally.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PaymentSyncService {

    private static final Logger log = LoggerFactory.getLogger(PaymentSyncService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public void syncPaymentsFromJson() {
        try {
            String folderPath = GetPropertyValue.getPropValue("TALLY", "bgrn", jdbcTemplate);
            File folder = new File(folderPath);

            if (!folder.exists() || !folder.isDirectory()) {
                log.error("❌ Invalid folder path: {}", folderPath);
                return;
            }

//            File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
            File[] jsonFiles = folder.listFiles((dir, name) -> name.equalsIgnoreCase("sr_payment_export.json"));

            if (jsonFiles == null || jsonFiles.length == 0) {
                log.info("📭 No payment JSON files found in {}", folderPath);
                return;
            }

            for (File jsonFile : jsonFiles) {
                log.info("📂 Processing payment file: {}", jsonFile.getName());
                boolean success = false;

                try {
                    JsonNode root = mapper.readTree(jsonFile);
                    JsonNode payments = root.path("Payment");

                    if (!payments.isArray()) {
                        log.warn("⚠️ No Payments array found in file: {}", jsonFile.getName());
                        continue;
                    }

                    for (JsonNode payment : payments) {
                        String partyName = payment.path("Party").asText(null);
                        String rawBillNo = payment.path("BillNo").asText(null);
                        LocalDate vouDate = parseDateSafe(payment, "VouDt");
                        String vouType = payment.path("VouType").asText("Payment");
                        String vouNo = payment.path("VouNo").asText();
                        BigDecimal billAmt = new BigDecimal(payment.path("BillAmt").asText("0")).abs();
                        BigDecimal totAmt = new BigDecimal(payment.path("TotAmt").asText("0")).abs();

                        if (partyName == null || rawBillNo == null) continue;

                        Integer partyId = getOrInsertPartyId(partyName);

                        String praCode = null;
                        String billNumber = rawBillNo;
                        if (rawBillNo.contains("-")) {
                            String[] parts = rawBillNo.split("-", 2);
                            praCode = parts[0].trim();
                            billNumber = parts[1].trim();
                        }
                        BigDecimal pendingBal = totAmt.subtract(billAmt);

                        Integer billId = insertBill(partyId, billNumber, praCode, pendingBal, totAmt);
                        insertVoucher(billId, vouNo, vouType, vouDate);
                    }

                    success = true;
                } catch (Exception ex) {
                    log.error("❌ Error processing file: {}", jsonFile.getName(), ex);
                } finally {
                    moveJsonFile(jsonFile, success);
                }
            }

        } catch (Exception e) {
            log.error("❌ Error during payment sync: ", e);
        }
    }

    private Integer getOrInsertPartyId(String partyName) {
        try {
            List<Integer> ids = jdbcTemplate.queryForList(
                    "SELECT PARTY_ID FROM payable_party_hdr WHERE PARTY_NAME = ?",
                    new Object[]{partyName}, Integer.class
            );

            if (!ids.isEmpty()) {
                return ids.get(0);
            }

            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO payable_party_hdr (PARTY_NAME, TENANT_ID, CREATED_DATETIME) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, partyName);
                ps.setString(2, "bgrn");
                ps.setString(3, CommonMethod.getCurrentDateTime());
                return ps;
            }, kh);

            return kh.getKey().intValue();

        } catch (Exception e) {
            log.error("Error inserting/retrieving party: {}", partyName, e);
            return null;
        }
    }

    private Integer insertBill(Integer partyId, String billNumber, String praCode, BigDecimal billAmt, BigDecimal totAmt) {
        try {
            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO payable_billing_dtl " +
                                "(PARTY_ID, PAYABLE_BILL_NO, TENANT_ID, CREATED_DATETIME, PRA_CODE, ENTRY_TYPE, PAYABLE_PENDING_BAL, STATUS, PAYABLE_OPEN_BAL) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, partyId);
                ps.setString(2, billNumber);
                ps.setString(3, "bgrn");
                ps.setString(4, CommonMethod.getCurrentDateTime());
                ps.setString(5, praCode);
                ps.setString(6, "PAID");
                ps.setBigDecimal(7, billAmt);
                ps.setString(8, "PAID");
                ps.setBigDecimal(9, totAmt);
                return ps;
            }, kh);
            return kh.getKey().intValue();
        } catch (Exception e) {
            log.error("Error inserting bill: {}", billNumber, e);
            return null;
        }
    }

    private void insertVoucher(Integer billId, String vouNo, String vouType, LocalDate vouDate) {
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO payable_voc_dtl " +
                                "(PAYABLE_BILL_ID, PAYABLE_VOC_NO, PAYABLE_VOC_TYPE, PAYABLE_VOC_DATE, TENANT_ID, CREATED_DATETIME) " +
                                "VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, billId);
                ps.setString(2, vouNo);
                ps.setString(3, vouType);
                ps.setDate(4, java.sql.Date.valueOf(vouDate));
                ps.setString(5, "bgrn");
                ps.setString(6, CommonMethod.getCurrentDateTime());
                return ps;
            });
        } catch (Exception e) {
            log.error("Error inserting voucher: {}", vouNo, e);
        }
    }

    private LocalDate parseDateSafe(JsonNode node, String field) {
        try {
            String val = node.get(field).asText();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH);
            return LocalDate.parse(val, fmt);
        } catch (Exception e) {
            return null;
        }
    }

    private void moveJsonFile(File jsonFile, boolean success) {
        try {
            String processedPath = GetPropertyValue.getPropValue("PAYMENT_PROCESSED_PATH", "bgrn", jdbcTemplate);
            String errorPath = GetPropertyValue.getPropValue("PAYMENT_ERROR_PATH", "bgrn", jdbcTemplate);

            String targetDir = success ? processedPath : errorPath;
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path targetPath = new File(dir, jsonFile.getName()).toPath();
            Files.move(jsonFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            if (success) {
                log.info("✅ Moved file {} to processed folder.", jsonFile.getName());
            } else {
                log.warn("🚨 Moved file {} to error folder.", jsonFile.getName());
            }

        } catch (Exception ex) {
            log.error("❌ Failed to move file {}.", jsonFile.getName(), ex);
        }
    }
}
