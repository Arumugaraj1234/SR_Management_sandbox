package com.vmfg.tally.service;

import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sets pra_hdr AMOUNT_DUE and paid status from the Tally Payment vouchers synced by TransactionSyncService.
 * Paid = sum of the PRA's bill allocations on Payment vouchers (Tally stores them as negative/debit).
 * Only approval-completed, non-cancelled PRAs are touched.
 */
@Service
public class PraUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PraUpdateService.class);

    private static final String TENANT_ID = "bgrn";
    private static final String PRA_CANCEL_STATUS = "DS168";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Recalculates every PRA referenced in the synced Tally vouchers (manual trigger)
    public void updatePraCompletionStatus() {
        List<String> praCodes = jdbcTemplate.queryForList(
                "SELECT DISTINCT PRA_CODE FROM tally_voucher_bill_dtl WHERE PRA_CODE IS NOT NULL", String.class);
        updatePraFromTally(praCodes);
    }

    public void updatePraFromTally(Collection<String> praCodes) {
        if (praCodes == null || praCodes.isEmpty()) {
            return;
        }
        try {
            logger.info("Starting PRA payment update for {} PRA code(s)...", praCodes.size());

            String closedStatus = GetPropertyValue.getPropValue("PRA_STATUS_UPDATE", TENANT_ID, jdbcTemplate);
            String closedSeq = GetPropertyValue.getPropValue("PRA_SEQ_UPDATE", TENANT_ID, jdbcTemplate);
            String partialStatus = GetPropertyValue.getPropValue("PRA_PARTIAL_STATUS", TENANT_ID, jdbcTemplate);
            String partialSeq = GetPropertyValue.getPropValue("PRA_PARTIAL_SEQ", TENANT_ID, jdbcTemplate);

            List<String> codes = new ArrayList<>(praCodes);
            Map<String, BigDecimal> paidMap = new HashMap<>();
            Map<String, Object> lastPaidDateMap = new HashMap<>();
            for (int i = 0; i < codes.size(); i += 500) {
                List<String> chunk = codes.subList(i, Math.min(i + 500, codes.size()));
                String paidSql = "SELECT b.PRA_CODE, -SUM(b.AMOUNT) AS PAID, MAX(h.VOUCHER_DATE) AS LAST_PAID_DATE " +
                        "FROM tally_voucher_bill_dtl b INNER JOIN tally_voucher_hdr h ON h.VOUCHER_ID = b.VOUCHER_ID " +
                        "WHERE h.VOUCHER_TYPE = 'Payment' AND b.PRA_CODE IN (" + String.join(",", Collections.nCopies(chunk.size(), "?")) + ") " +
                        "GROUP BY b.PRA_CODE";
                for (Map<String, Object> row : jdbcTemplate.queryForList(paidSql, chunk.toArray())) {
                    paidMap.put(row.get("PRA_CODE").toString(), new BigDecimal(row.get("PAID").toString()));
                    lastPaidDateMap.put(row.get("PRA_CODE").toString(), row.get("LAST_PAID_DATE"));
                }
            }

            int closed = 0, partial = 0, reverted = 0;
            for (String praCode : codes) {
                List<Map<String, Object>> praRows = jdbcTemplate.queryForList(
                        "SELECT PRA_ID, AMOUNT_PAYABLE, AMOUNT_DUE, STATUS_CODE FROM pra_hdr " +
                                "WHERE PRA_CODE = ? AND TENANT_ID = ? AND IS_COMPLETED = 1 AND STATUS_CODE <> ?",
                        praCode, TENANT_ID, PRA_CANCEL_STATUS);
                if (praRows.isEmpty()) {
                    logger.warn("⚠ PRA {} not found in pra_hdr (or not completed / cancelled).", praCode);
                    continue;
                }

                BigDecimal paid = paidMap.containsKey(praCode) ? paidMap.get(praCode) : BigDecimal.ZERO;

                for (Map<String, Object> pra : praRows) {
                    Object praId = pra.get("PRA_ID");
                    BigDecimal amountPayable = pra.get("AMOUNT_PAYABLE") != null ?
                            new BigDecimal(pra.get("AMOUNT_PAYABLE").toString()) : BigDecimal.ZERO;
                    String statusCode = pra.get("STATUS_CODE") != null ? pra.get("STATUS_CODE").toString() : "";

                    if (paid.signum() <= 0) {
                        // Payment removed in Tally: put a previously paid PRA back to its last approval status
                        if (statusCode.equals(closedStatus) || statusCode.equals(partialStatus)) {
                            jdbcTemplate.update("UPDATE pra_hdr h " +
                                            "INNER JOIN pra_status s ON s.PRA_S_ID = (SELECT MAX(PRA_S_ID) FROM pra_status WHERE PRA_ID = h.PRA_ID) " +
                                            "SET h.STATUS = s.SEQUENCE_NO, h.STATUS_CODE = s.SEQUENCE_STATUS, h.AMOUNT_DUE = h.AMOUNT_PAYABLE, " +
                                            "h.LAST_UPDATED_DATETIME = ? WHERE h.PRA_ID = ?",
                                    CommonMethod.getCurrentDateTime(), praId);
                            reverted++;
                            logger.info("↩ PRA {} has no Tally payment any more, status restored.", praCode);
                        }
                        continue;
                    }

                    BigDecimal amountDue = amountPayable.subtract(paid).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    // Tally pays in whole rupees (74462 against 74462.72), so under Rs.1 left counts as fully paid
                    boolean fullyPaid = amountPayable.subtract(paid).compareTo(BigDecimal.ONE) < 0;

                    if (fullyPaid) {
                        // COMPLETED_DATETIME = last payment date, used for the dashboard over-due days
                        jdbcTemplate.update("UPDATE pra_hdr SET STATUS = ?, STATUS_CODE = ?, AMOUNT_DUE = ?, " +
                                        "LAST_UPDATED_DATETIME = ?, COMPLETED_DATETIME = ? WHERE PRA_ID = ?",
                                closedSeq, closedStatus, BigDecimal.ZERO, CommonMethod.getCurrentDateTime(),
                                lastPaidDateMap.get(praCode), praId);
                        closed++;
                        logger.info("✅ PRA {} closed | Amount Payable: {} | Paid: {}", praCode, amountPayable, paid);
                    } else {
                        jdbcTemplate.update("UPDATE pra_hdr SET STATUS = ?, STATUS_CODE = ?, AMOUNT_DUE = ?, " +
                                        "LAST_UPDATED_DATETIME = ? WHERE PRA_ID = ?",
                                partialSeq, partialStatus, amountDue, CommonMethod.getCurrentDateTime(), praId);
                        partial++;
                        logger.info("PRA {} partially paid | Amount Payable: {} | Paid: {} | Due: {}", praCode, amountPayable, paid, amountDue);
                    }
                }
            }

            logger.info("PRA payment update finished: {} closed, {} partially paid, {} restored.", closed, partial, reverted);
        } catch (Exception e) {
            logger.error("Error while updating PRA payment status from Tally", e);
        }
    }
}
