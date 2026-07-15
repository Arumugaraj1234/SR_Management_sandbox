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
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReceivableSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ReceivableSyncService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void syncReceivablesFromJson() {
        String folderPath = GetPropertyValue.getPropValue("TALLY", "bgrn", jdbcTemplate);
        logger.info("Folder Path: {}", folderPath);

        File jsonDir = new File(folderPath);
//        File[] jsonFiles = jsonDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        File[] jsonFiles = jsonDir.listFiles((dir, name) ->
                name.toLowerCase().startsWith("sr_rec_export") && name.toLowerCase().endsWith(".json")
        );

        if (!jsonDir.exists()) {
            throw new RuntimeException("Directory does not exist: " + jsonDir.getAbsolutePath());
        }
        if (!jsonDir.isDirectory()) {
            throw new RuntimeException("Path is not a directory: " + jsonDir.getAbsolutePath());
        }
        if (jsonFiles == null || jsonFiles.length == 0) return;

        Arrays.sort(jsonFiles, Comparator.comparingLong(File::lastModified).reversed());

        File jsonFile = Arrays.stream(Objects.requireNonNull(jsonDir.listFiles()))
                .filter(file -> file.isFile() && file.getName().equalsIgnoreCase("SR_rec_export.json"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("SR_rec_export.json not found in folder: " + jsonDir.getAbsolutePath()));

        logger.info("Selected JSON file: {}", jsonFile.getAbsolutePath());

        boolean success = false;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonFile);
            JsonNode bills = root.get("Bills");

            if (bills != null && bills.isArray()) {
                for (JsonNode bill : bills) {
                    // --- Party ---
                    String partyName = bill.has("Party") ? bill.get("Party").asText() : null;
                    if (partyName == null || partyName.trim().isEmpty()) continue;

                    Integer partyId = getOrInsertPartyId(partyName);

                    // --- Bill ---
                    String fullBillNumber = bill.has("Bill Number") ? bill.get("Bill Number").asText() : null;
                    if (fullBillNumber == null || fullBillNumber.trim().isEmpty()) continue;

                    String projectCode = null;
                    String billNumber = fullBillNumber;

                    if (fullBillNumber.contains("-")) {
                        String[] parts = fullBillNumber.split("-", 2);
                        projectCode = parts[0].trim().replaceAll("[^A-Za-z0-9]", "");
                        billNumber = parts[1].trim();
                    }

                    LocalDate billDate = parseDateSafe(bill, "Bill Date");
                    LocalDate dueDate = parseDateSafe(bill, "Due Date");
                    BigDecimal openBal = parseDecimalSafe(bill, "Opening Balance");
                    BigDecimal pendingBal = parseDecimalSafe(bill, "Pending Amount");

                    String status = "PENDING";
                    if (pendingBal != null && pendingBal.compareTo(BigDecimal.ZERO) == 0) {
                        status = "PAID";
                    }

                    // ✅ Always insert new Bill
                    Integer billId = insertBill(
                            partyId, billNumber, billDate, openBal, pendingBal, dueDate, status, projectCode
                    );

                    // --- Vouchers ---
                    JsonNode vchList = bill.get("Vch");
                    if (vchList != null && vchList.isArray()) {
                        for (JsonNode vch : vchList) {
                            LocalDate vchDate = parseDateSafe(vch, "Vch Date");
                            String vchType = vch.has("Vch Type") ? vch.get("Vch Type").asText() : null;
                            String vchNum = vch.has("Vch Num") ? vch.get("Vch Num").asText() : null;

                            if (vchNum == null || vchNum.trim().isEmpty()) continue;

                            // ✅ Always insert new Voucher
                            insertVch(billId, vchNum, vchType, vchDate);
                        }
                    }
                }
            }

            logger.info("Receivable sync completed successfully at {}", new Date());
            success = true;

        } catch (Exception e) {
            logger.error("Error during receivable sync: {}", e.getMessage(), e);
        } finally {
            if (jsonFile != null) {
                moveJsonFile(jsonFile, success);
            }
        }
    }

    // --- Party: insert only if not exists ---
    private Integer getOrInsertPartyId(String partyName) {
        List<Integer> ids = jdbcTemplate.queryForList(
                "SELECT PARTY_ID FROM receivable_party_hdr WHERE PARTY_NAME = ?",
                new Object[]{partyName}, Integer.class
        );

        if (!ids.isEmpty()) return ids.get(0);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO receivable_party_hdr (PARTY_NAME, TENANT_ID, CREATED_DATETIME) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, partyName);
            ps.setString(2, "bgrn");
            ps.setString(3, CommonMethod.getCurrentDateTime());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // --- Bill: always insert ---
    private Integer insertBill(Integer partyId, String billNumber, LocalDate billDate,
                               BigDecimal openBal, BigDecimal pendingBal, LocalDate dueDate, String status, String projectCode) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO receivable_billing_dtl (PARTY_ID, RECEIVABLE_BILL_NO, RECEIVABLE_BILL_DATE, RECEIVABLE_OPEN_BAL, RECEIVABLE_PENDING_BAL, RECEIVABLE_DUE_DATE, TENANT_ID, STATUS, CREATED_DATETIME, PROJECT_CODE, ENTRY_TYPE) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, partyId);
            ps.setString(2, billNumber);
            ps.setObject(3, billDate);
            ps.setBigDecimal(4, openBal);
            ps.setBigDecimal(5, pendingBal);
            ps.setObject(6, dueDate);
            ps.setString(7, "bgrn");
            ps.setString(8, status);
            ps.setString(9, CommonMethod.getCurrentDateTime());
            ps.setString(10, projectCode);
            ps.setString(11,"PENDING");
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // --- Voucher: always insert ---
    private void insertVch(Integer billId, String vchNum, String vchType, LocalDate vchDate) {
        jdbcTemplate.update(
                "INSERT INTO receivable_voc_dtl (RECEIVABLE_BILL_ID, RECEIVABLE_VOC_NO, RECEIVABLE_VOC_TYPE, RECEIVABLE_VOC_DATE, TENANT_ID, CREATED_DATETIME) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                billId, vchNum, vchType, vchDate, "bgrn", CommonMethod.getCurrentDateTime()
        );
    }

    // --- Helpers ---
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
            if (!node.has(field)) return null;
            String val = node.get(field).asText();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH);
            return LocalDate.parse(val, fmt);
        } catch (Exception e) {
            return null;
        }
    }

    private void moveJsonFile(File file, boolean isSuccess) {
        String PROCESSED_PATH = GetPropertyValue.getPropValue("RECEIVABLE_PARTYWISE_PROCESSED_PATH", "bgrn", jdbcTemplate);
        String ERROR_PATH = GetPropertyValue.getPropValue("RECIEVABLE_PARTYWISE_ERROR_PATH", "bgrn", jdbcTemplate);
        logger.info("Processed Path: {}", PROCESSED_PATH);
        logger.info("Error Path: {}", ERROR_PATH);
        String destinationDir = isSuccess ? PROCESSED_PATH : ERROR_PATH;

        String timestamp = java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String originalName = file.getName();
        String baseName = originalName.contains(".")
                ? originalName.substring(0, originalName.lastIndexOf("."))
                : originalName;
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";

        String newFileName = baseName + "_" + timestamp + extension;

        File destDir = new File(destinationDir);
        if (!destDir.exists()) destDir.mkdirs();

        File destFile = new File(destDir, newFileName);
        if (file.renameTo(destFile)) {
            System.out.println("Moved and renamed file to " + destFile.getAbsolutePath());
        } else {
            System.err.println("Failed to move file to " + destFile.getAbsolutePath());
        }
    }
}