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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReceiptSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptSyncService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void syncReceiptsFromJson() {
        String folderPath = GetPropertyValue.getPropValue("TALLY", "bgrn", jdbcTemplate);
        File jsonDir = new File(folderPath);

        if (!jsonDir.exists() || !jsonDir.isDirectory()) {
            logger.error("Invalid receipt JSON directory: {}", jsonDir.getAbsolutePath());
            return;
        }

//        File[] jsonFiles = jsonDir.listFiles((d, n) -> n.toLowerCase().endsWith(".json"));
        File[] jsonFiles = jsonDir.listFiles((dir, name) ->
                name.toLowerCase().startsWith("sr_receipt_export") && name.toLowerCase().endsWith(".json")
        );

        if (jsonFiles == null || jsonFiles.length == 0) {
            logger.warn("No Receipt JSON files found in {}", jsonDir.getAbsolutePath());
            return;
        }

        Arrays.sort(jsonFiles, Comparator.comparingLong(File::lastModified).reversed());
        File jsonFile = Arrays.stream(Objects.requireNonNull(jsonDir.listFiles()))
                .filter(file -> file.isFile() && file.getName().equalsIgnoreCase("sr_receipt_export.json"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("sr_receipt_export.json not found in folder: " + jsonDir.getAbsolutePath()));
        logger.info("Processing Receipt JSON: {}", jsonFile.getName());

        boolean success = false;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonFile);
            JsonNode receipts = root.get("Receipt");

            if (receipts != null && receipts.isArray()) {
                for (JsonNode receipt : receipts) {
                    String partyName = receipt.path("Party").asText(null);
                    if (partyName == null) continue;

                    // Lookup or Insert party
                    Integer partyId = getOrInsertPartyId(partyName);

                    String rawBillNo = receipt.path("BillNo").asText(null);
                    if (rawBillNo == null) continue;

                    String projectCode = null;
                    String billNumber = rawBillNo;
                    if (rawBillNo.contains("-")) {
                        String[] parts = rawBillNo.split("-", 2);
                        projectCode = parts[0].trim();
                        billNumber = parts[1].trim();
                    }

                    LocalDate vouDate = parseDateSafe(receipt, "VouDt");
                    String vouType = receipt.path("VouType").asText("Receipt");
                    String vouNo = receipt.path("VouNo").asText();
                    BigDecimal billAmt = parseDecimalSafe(receipt, "BillAmt");
                    BigDecimal totAmt = parseDecimalSafe(receipt, "TotAmt");
                    String status = "PENDING";
                    BigDecimal pendingBal = totAmt.subtract(billAmt);
                    if (pendingBal != null && pendingBal.compareTo(BigDecimal.ZERO) == 0) {
                        status = "PAID";
                    }

                    // Insert bill (always new)
                    Integer billId = insertBill(partyId, billNumber, projectCode, totAmt, pendingBal, status);

                    // Insert voucher (always new)
                    insertVoucher(billId, vouNo, vouType, vouDate, billAmt);
                }
            }

            success = true;
            logger.info("Receipt sync completed.");
        } catch (Exception e) {
            logger.error("Error in ReceiptSyncService", e);
        } finally {
            moveJsonFile(jsonFile, success);
        }
    }

    /**
     * If party exists, return ID. If not, insert into receivable_party_hdr and return new ID.
     */
    private Integer getOrInsertPartyId(String partyName) {
        List<Integer> ids = jdbcTemplate.queryForList(
                "SELECT PARTY_ID FROM receivable_party_hdr WHERE PARTY_NAME = ?",
                new Object[]{partyName}, Integer.class
        );
        if (!ids.isEmpty()) {
            return ids.get(0);
        }

        // Insert new party if not found
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO receivable_party_hdr (PARTY_NAME, TENANT_ID, CREATED_DATETIME) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, partyName);
            ps.setString(2, "bgrn");
            ps.setString(3, CommonMethod.getCurrentDateTime());
            return ps;
        }, kh);

        Integer newId = kh.getKey().intValue();
        logger.info("Inserted new Party: {} (ID={})", partyName, newId);
        return newId;
    }

    private Integer insertBill(Integer partyId, String billNumber, String projectCode, BigDecimal openingBal,BigDecimal pendingBal, String status) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO receivable_billing_dtl (PARTY_ID, RECEIVABLE_BILL_NO, TENANT_ID, CREATED_DATETIME, PROJECT_CODE, ENTRY_TYPE, RECEIVABLE_OPEN_BAL, RECEIVABLE_PENDING_BAL, STATUS) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, partyId);
            ps.setString(2, billNumber);
            ps.setString(3, "bgrn");
            ps.setString(4, CommonMethod.getCurrentDateTime());
            ps.setString(5, projectCode);
            ps.setString(6, "PAID");
            ps.setBigDecimal(7, openingBal);
            ps.setBigDecimal(8, pendingBal);
            ps.setString(9, status);
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    private void insertVoucher(Integer billId, String vouNo, String vouType, LocalDate vouDate, BigDecimal amt) {
        jdbcTemplate.update(
                "INSERT INTO receivable_voc_dtl (RECEIVABLE_BILL_ID, RECEIVABLE_VOC_NO, RECEIVABLE_VOC_TYPE, RECEIVABLE_VOC_DATE, TENANT_ID, CREATED_DATETIME) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                billId, vouNo, vouType,
                vouDate != null ? Date.valueOf(vouDate) : null,
                "bgrn", CommonMethod.getCurrentDateTime()
        );
    }

    private BigDecimal parseDecimalSafe(JsonNode node, String field) {
        try {
            if (!node.has(field)) return BigDecimal.ZERO;
            String raw = node.get(field).asText().trim();
            // Remove commas but keep decimals
            raw = raw.replace(",", "");
            return new BigDecimal(raw);
        } catch (Exception e) {
            return BigDecimal.ZERO;
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

    private void moveJsonFile(File file, boolean isSuccess) {
        String processed = GetPropertyValue.getPropValue("RECEIPT_PROCESSED_PATH", "bgrn", jdbcTemplate);
        String error = GetPropertyValue.getPropValue("RECEIPT_ERROR_PATH", "bgrn", jdbcTemplate);
        String dest = isSuccess ? processed : error;
        File destDir = new File(dest);
        if (!destDir.exists()) destDir.mkdirs();

        String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File destFile = new File(destDir, file.getName().replace(".json", "_" + timestamp + ".json"));
        file.renameTo(destFile);
    }
}