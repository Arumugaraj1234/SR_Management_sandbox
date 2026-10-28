package com.vmfg.tally.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Syncs the native Tally voucher export (Transactions.json, {"tallymessage":[...]}) into
 * tally_voucher_hdr / tally_voucher_bill_dtl, keyed by voucher GUID.
 * The file's earliest..latest voucher date is treated as the export range: inside it, vouchers are
 * inserted / updated (alterid changed) / deleted (missing from the file or cancelled/deleted in Tally);
 * vouchers outside the range are not touched. PRA amount due/status is recalculated afterwards.
 */
@Service
public class TransactionSyncService {

    private static final Logger log = LoggerFactory.getLogger(TransactionSyncService.class);

    private static final String TENANT_ID = "bgrn";
    private static final String FILE_NAME = "Transactions.json";
    // Bill name like "PRA4984-1107/115" -> PRA code PRA4984, project 1107
    private static final Pattern PRA_PATTERN = Pattern.compile("^\\s*PRA\\s*(\\d+)\\s*(?:-\\s*([^/\\s]+)\\s*/)?", Pattern.CASE_INSENSITIVE);
    private static final DateTimeFormatter TALLY_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private PraUpdateService praUpdateService;

    private final ObjectMapper mapper = new ObjectMapper();

    public synchronized void syncTransactionsFromJson() {
        try {
            String folderPath = GetPropertyValue.getPropValue("TALLY", TENANT_ID, jdbcTemplate);
            File folder = new File(folderPath);

            if (!folder.exists() || !folder.isDirectory()) {
                log.error("❌ Invalid folder path: {}", folderPath);
                return;
            }

            File[] jsonFiles = folder.listFiles((dir, name) -> name.equalsIgnoreCase(FILE_NAME));

            if (jsonFiles == null || jsonFiles.length == 0) {
                log.info("📭 No {} found in {}", FILE_NAME, folderPath);
                return;
            }

            for (File jsonFile : jsonFiles) {
                log.info("📂 Processing Tally transaction file: {}", jsonFile.getName());
                boolean success = false;
                Set<String> affectedPraCodes = new HashSet<>();

                try {
                    List<TallyVoucher> vouchers = readVouchers(jsonFile);
                    validate(vouchers);

                    TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
                    SyncResult result = txTemplate.execute(status -> applyVouchers(vouchers, affectedPraCodes));

                    log.info("✅ Tally transactions {} -> {}: {} in file, {} inserted, {} updated, {} unchanged, {} deleted",
                            result.fromDate, result.toDate, vouchers.size(), result.inserted, result.updated,
                            result.unchanged, result.deleted);
                    success = true;
                } catch (Exception ex) {
                    log.error("❌ Error processing file: {}", jsonFile.getName(), ex);
                } finally {
                    moveJsonFile(jsonFile, success);
                }

                if (success) {
                    praUpdateService.updatePraFromTally(affectedPraCodes);
                }
            }

        } catch (Exception e) {
            log.error("❌ Error during Tally transaction sync: ", e);
        }
    }

    // Streams the tallymessage array so a full-year export is not held as one big tree
    private List<TallyVoucher> readVouchers(File jsonFile) throws Exception {
        List<TallyVoucher> vouchers = new ArrayList<>();
        try (InputStream in = Files.newInputStream(jsonFile.toPath());
             JsonParser parser = mapper.getFactory().createParser(in)) {

            boolean found = false;
            while (parser.nextToken() != null) {
                if (parser.currentToken() == JsonToken.FIELD_NAME && "tallymessage".equals(parser.getCurrentName())) {
                    found = true;
                    if (parser.nextToken() != JsonToken.START_ARRAY) {
                        throw new IllegalStateException("tallymessage is not an array");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {
                        JsonNode node = mapper.readTree(parser);
                        // Only vouchers carry a voucher type; masters (ledgers etc.) in the same export are ignored
                        if (node.hasNonNull("guid") && node.hasNonNull("vouchertypename")) {
                            vouchers.add(toVoucher(node));
                        }
                    }
                    break;
                }
            }
            if (!found) {
                throw new IllegalStateException("tallymessage array not found");
            }
        }
        return vouchers;
    }

    private TallyVoucher toVoucher(JsonNode node) {
        TallyVoucher v = new TallyVoucher();
        v.guid = node.path("guid").asText().trim();
        v.alterId = parseLong(node.path("alterid").asText(null));
        v.masterId = parseLong(node.path("masterid").asText(null));
        String date = node.path("date").asText("").trim();
        v.voucherDate = date.isEmpty() ? null : LocalDate.parse(date, TALLY_DATE);
        v.voucherNo = trimToNull(node.path("vouchernumber").asText(null));
        v.voucherType = trimToNull(node.path("vouchertypename").asText(null));
        v.partyLedger = trimToNull(node.path("partyledgername").asText(null));
        v.narration = trimToNull(node.path("narration").asText(null));
        v.active = !isYes(node.path("iscancelled")) && !isYes(node.path("isdeleted")) && !isYes(node.path("isoptional"));

        for (String entriesField : new String[]{"allledgerentries", "ledgerentries"}) {
            for (JsonNode entry : node.path(entriesField)) {
                String ledgerName = trimToNull(entry.path("ledgername").asText(null));
                for (JsonNode bill : entry.path("billallocations")) {
                    TallyBill b = new TallyBill();
                    b.ledgerName = ledgerName;
                    b.billName = trimToNull(bill.path("name").asText(null));
                    b.billType = trimToNull(bill.path("billtype").asText(null));
                    b.amount = parseAmount(bill.path("amount").asText(null));
                    if (b.billName != null) {
                        Matcher m = PRA_PATTERN.matcher(b.billName);
                        if (m.find()) {
                            b.praCode = "PRA" + m.group(1);
                            b.projectCode = m.group(2);
                        }
                    }
                    v.bills.add(b);
                }
            }
        }
        return v;
    }

    private void validate(List<TallyVoucher> vouchers) {
        if (vouchers.isEmpty()) {
            // An empty file would otherwise delete nothing, but refuse it so a broken export is noticed
            throw new IllegalStateException("No vouchers in file");
        }
        Set<String> guids = new HashSet<>();
        for (TallyVoucher v : vouchers) {
            if (v.guid.isEmpty() || v.voucherDate == null) {
                throw new IllegalStateException("Voucher without guid/date: " + v.guid + " / " + v.voucherNo);
            }
            if (!guids.add(v.guid)) {
                throw new IllegalStateException("Duplicate voucher guid in file: " + v.guid);
            }
        }
    }

    private SyncResult applyVouchers(List<TallyVoucher> vouchers, Set<String> affectedPraCodes) {
        SyncResult result = new SyncResult();
        for (TallyVoucher v : vouchers) {
            if (result.fromDate == null || v.voucherDate.isBefore(result.fromDate)) result.fromDate = v.voucherDate;
            if (result.toDate == null || v.voucherDate.isAfter(result.toDate)) result.toDate = v.voucherDate;
        }

        // Existing vouchers inside the file's range, plus any whose guid is in the file (date may have moved)
        Map<String, Object[]> existing = new HashMap<>();
        jdbcTemplate.query("SELECT VOUCHER_ID, GUID, ALTER_ID FROM tally_voucher_hdr WHERE VOUCHER_DATE BETWEEN ? AND ?",
                rs -> { existing.put(rs.getString("GUID"), new Object[]{rs.getInt("VOUCHER_ID"), (Long) rs.getObject("ALTER_ID")}); },
                java.sql.Date.valueOf(result.fromDate), java.sql.Date.valueOf(result.toDate));
        List<String> fileGuids = new ArrayList<>();
        for (TallyVoucher v : vouchers) fileGuids.add(v.guid);
        for (List<String> chunk : chunks(fileGuids, 500)) {
            jdbcTemplate.query("SELECT VOUCHER_ID, GUID, ALTER_ID FROM tally_voucher_hdr WHERE GUID IN (" + placeholders(chunk.size()) + ")",
                    rs -> { existing.put(rs.getString("GUID"), new Object[]{rs.getInt("VOUCHER_ID"), (Long) rs.getObject("ALTER_ID")}); },
                    chunk.toArray());
        }

        List<Integer> deleteIds = new ArrayList<>();
        List<Integer> replaceBillIds = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        String now = CommonMethod.getCurrentDateTime();

        for (TallyVoucher v : vouchers) {
            seen.add(v.guid);
            Object[] row = existing.get(v.guid);
            if (!v.active) {
                if (row != null) deleteIds.add((Integer) row[0]);
                continue;
            }
            for (TallyBill b : v.bills) {
                if (b.praCode != null) affectedPraCodes.add(b.praCode);
            }
            if (row == null) {
                int voucherId = insertHeader(v, now);
                insertBills(voucherId, v.bills);
                result.inserted++;
            } else if (v.alterId != null && v.alterId.equals(row[1])) {
                result.unchanged++;
            } else {
                int voucherId = (Integer) row[0];
                jdbcTemplate.update("UPDATE tally_voucher_hdr SET ALTER_ID = ?, MASTER_ID = ?, VOUCHER_DATE = ?, VOUCHER_NO = ?, " +
                                "VOUCHER_TYPE = ?, PARTY_LEDGER = ?, NARRATION = ?, UPDATED_DATETIME = ? WHERE VOUCHER_ID = ?",
                        v.alterId, v.masterId, java.sql.Date.valueOf(v.voucherDate), v.voucherNo, v.voucherType,
                        v.partyLedger, v.narration, now, voucherId);
                replaceBillIds.add(voucherId);
                result.updated++;
            }
        }

        // In range but no longer exported -> deleted in Tally
        for (Map.Entry<String, Object[]> e : existing.entrySet()) {
            if (!seen.contains(e.getKey())) deleteIds.add((Integer) e.getValue()[0]);
        }

        // PRA codes on the old bill rows also need recalculating (payment removed or moved to another PRA)
        List<Integer> touchedIds = new ArrayList<>(deleteIds);
        touchedIds.addAll(replaceBillIds);
        for (List<Integer> chunk : chunks(touchedIds, 500)) {
            affectedPraCodes.addAll(jdbcTemplate.queryForList(
                    "SELECT DISTINCT PRA_CODE FROM tally_voucher_bill_dtl WHERE PRA_CODE IS NOT NULL AND VOUCHER_ID IN (" + placeholders(chunk.size()) + ")",
                    String.class, chunk.toArray()));
        }

        for (List<Integer> chunk : chunks(replaceBillIds, 500)) {
            jdbcTemplate.update("DELETE FROM tally_voucher_bill_dtl WHERE VOUCHER_ID IN (" + placeholders(chunk.size()) + ")", chunk.toArray());
        }
        Set<Integer> replaceBillIdSet = new HashSet<>(replaceBillIds);
        for (TallyVoucher v : vouchers) {
            Object[] row = existing.get(v.guid);
            if (row != null && replaceBillIdSet.contains(row[0])) {
                insertBills((Integer) row[0], v.bills);
            }
        }

        for (List<Integer> chunk : chunks(deleteIds, 500)) {
            // bill rows go with the header (ON DELETE CASCADE)
            jdbcTemplate.update("DELETE FROM tally_voucher_hdr WHERE VOUCHER_ID IN (" + placeholders(chunk.size()) + ")", chunk.toArray());
        }
        result.deleted = deleteIds.size();
        return result;
    }

    private int insertHeader(TallyVoucher v, String now) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO tally_voucher_hdr (GUID, ALTER_ID, MASTER_ID, VOUCHER_DATE, VOUCHER_NO, VOUCHER_TYPE, " +
                            "PARTY_LEDGER, NARRATION, TENANT_ID, CREATED_DATETIME, UPDATED_DATETIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, v.guid);
            ps.setObject(2, v.alterId);
            ps.setObject(3, v.masterId);
            ps.setDate(4, java.sql.Date.valueOf(v.voucherDate));
            ps.setString(5, v.voucherNo);
            ps.setString(6, v.voucherType);
            ps.setString(7, v.partyLedger);
            ps.setString(8, v.narration);
            ps.setString(9, TENANT_ID);
            ps.setString(10, now);
            ps.setString(11, now);
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    private void insertBills(int voucherId, List<TallyBill> bills) {
        if (bills.isEmpty()) return;
        List<Object[]> args = new ArrayList<>();
        for (TallyBill b : bills) {
            args.add(new Object[]{voucherId, b.ledgerName, b.billName, b.billType, b.amount, b.praCode, b.projectCode, TENANT_ID});
        }
        jdbcTemplate.batchUpdate("INSERT INTO tally_voucher_bill_dtl (VOUCHER_ID, LEDGER_NAME, BILL_NAME, BILL_TYPE, AMOUNT, " +
                "PRA_CODE, PROJECT_CODE, TENANT_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", args);
    }

    private void moveJsonFile(File jsonFile, boolean success) {
        try {
            String processedPath = GetPropertyValue.getPropValue("TRANSACTION_PROCESSED_PATH", TENANT_ID, jdbcTemplate);
            String errorPath = GetPropertyValue.getPropValue("TRANSACTION_ERROR_PATH", TENANT_ID, jdbcTemplate);

            String targetDir = success ? processedPath : errorPath;
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Timestamp prefix keeps every day's export instead of overwriting the previous one
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path targetPath = new File(dir, stamp + "_" + jsonFile.getName()).toPath();
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

    private static boolean isYes(JsonNode node) {
        if (node.isBoolean()) return node.asBoolean();
        String s = node.asText("").trim();
        return "yes".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s);
    }

    private static Long parseLong(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static BigDecimal parseAmount(String s) {
        if (s == null || s.trim().isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(s.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static String placeholders(int n) {
        return String.join(",", Collections.nCopies(n, "?"));
    }

    private static <T> List<List<T>> chunks(List<T> list, int size) {
        List<List<T>> out = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            out.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return out;
    }

    private static class TallyVoucher {
        String guid;
        Long alterId;
        Long masterId;
        LocalDate voucherDate;
        String voucherNo;
        String voucherType;
        String partyLedger;
        String narration;
        boolean active;
        List<TallyBill> bills = new ArrayList<>();
    }

    private static class TallyBill {
        String ledgerName;
        String billName;
        String billType;
        BigDecimal amount;
        String praCode;
        String projectCode;
    }

    private static class SyncResult {
        LocalDate fromDate;
        LocalDate toDate;
        int inserted;
        int updated;
        int unchanged;
        int deleted;
    }
}
