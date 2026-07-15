package com.vmfg.tally.service;

import com.vmfg.util.CommonMethod;
import com.vmfg.util.GetPropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class PraUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(PraUpdateService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updatePraCompletionStatus() {
        try {
            logger.info("Starting PRA completion check...");

            // 1. Get latest entry per PRA_CODE using CREATED_DATETIME
//            String fetchSql =
//                    "SELECT t.PAYABLE_BILL_ID, t.PRA_CODE, t.PAYABLE_OPEN_BAL, t.PAYABLE_PENDING_BAL " +
//                            "FROM payable_billing_dtl t " +
//                            "INNER JOIN ( " +
//                            "   SELECT PRA_CODE, MAX(CREATED_DATETIME) AS max_created " +
//                            "   FROM payable_billing_dtl " +
//                            "   GROUP BY PRA_CODE " +
//                            ") latest ON t.PRA_CODE = latest.PRA_CODE AND t.CREATED_DATETIME = latest.max_created";
            String fetchSql = "SELECT \n" +
                    "\tPAYABLE_BILL_ID,MAX(PAYABLE_OPEN_BAL) AS PAYABLE_OPEN_BAL,\n" +
                    "    PRA_CODE,\n" +
                    "    SUM(PAYABLE_PENDING_BAL) AS total_pending_balance\n" +
                    "FROM \n" +
                    "    payable_billing_dtl\n" +
                    "WHERE \n" +
                    "    ENTRY_TYPE = 'PAID'\n" +
                    "GROUP BY \n" +
                    "    PRA_CODE;";

            List<Map<String, Object>> payableRows = jdbcTemplate.queryForList(fetchSql);

            for (Map<String, Object> row : payableRows) {
                String praCode = row.get("PRA_CODE") != null ? row.get("PRA_CODE").toString() : null;
                BigDecimal openBal = row.get("PAYABLE_OPEN_BAL") != null ?
                        new BigDecimal(row.get("PAYABLE_OPEN_BAL").toString()) : BigDecimal.ZERO;
                BigDecimal pendingBal = row.get("total_pending_balance") != null ?
                        new BigDecimal(row.get("total_pending_balance").toString()) : BigDecimal.ZERO;

                if (praCode == null || praCode.trim().isEmpty()) {
                    continue;
                }

                // 2. Check existence in pra_hdr
                String checkSql = "SELECT COUNT(*) FROM pra_hdr WHERE PRA_CODE = ?";
                Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, praCode);

                    // 3. Get amount_payable from pra_hdr (optional, for logging)
                BigDecimal amountPayable = BigDecimal.ZERO;
                try {
                    String amtSql = "SELECT AMOUNT_PAYABLE FROM pra_hdr WHERE PRA_CODE = ?";
                    amountPayable = jdbcTemplate.queryForObject(amtSql, BigDecimal.class, praCode);
                } catch (EmptyResultDataAccessException e) {
                    logger.warn("⚠ No PRA record found for PRA_CODE: {}", praCode);
                }
                if (count != null && count > 0) {

                    logger.info("PRA {} found in pra_hdr | Amount Payable: {} | Open Bal: {} | Pending Bal: {}",
                            praCode, amountPayable, openBal, pendingBal);

                    // 4. If (OPEN_BAL - PENDING_BAL) == 0, update pra_hdr
                    BigDecimal pendingBalRounded = pendingBal.setScale(0, RoundingMode.HALF_UP);
                    BigDecimal amountPayableRounded = amountPayable.setScale(0, RoundingMode.HALF_UP);

                    if (pendingBalRounded.compareTo(amountPayableRounded) == 0) {
                        String praStatus = GetPropertyValue.getPropValue("PRA_STATUS_UPDATE", "bgrn", jdbcTemplate);
                        String praSeq = GetPropertyValue.getPropValue("PRA_SEQ_UPDATE", "bgrn", jdbcTemplate);

                        String updateSql = "UPDATE pra_hdr " +
                                "SET STATUS = ?, STATUS_CODE = ?, AMOUNT_DUE=?, LAST_UPDATED_DATETIME = ?, COMPLETED_DATETIME=? " +
                                "WHERE PRA_CODE = ?";
                        jdbcTemplate.update(updateSql, praSeq, praStatus, amountPayable.subtract(pendingBal), CommonMethod.getCurrentDateTime(), CommonMethod.getCurrentDateTime(), praCode);

                        logger.info("✅ PRA {} marked as completed (OPEN_BAL - PENDING_BAL = 0).", praCode);

                    }
                    else{
                        String praStatus = GetPropertyValue.getPropValue("PRA_PARTIAL_STATUS", "bgrn", jdbcTemplate);
                        String praSeq = GetPropertyValue.getPropValue("PRA_PARTIAL_SEQ", "bgrn", jdbcTemplate);

                        String updateSql = "UPDATE pra_hdr " +
                                "SET STATUS = ?, STATUS_CODE = ?, AMOUNT_DUE = ?, LAST_UPDATED_DATETIME = ? " +
                                "WHERE PRA_CODE = ?";
                        jdbcTemplate.update(updateSql, praSeq, praStatus, amountPayable.subtract(pendingBal), CommonMethod.getCurrentDateTime(), praCode);
                        logger.warn("⚠ PRA {} not found in pra_hdr table.", praCode);
                    }
                } else {
                    logger.warn("⚠ PRA {} not found in pra_hdr table.", praCode);
                }
            }

            logger.info("PRA completion check finished.");
        } catch (Exception e) {
            logger.error("Error while updating PRA completion status", e);
        }
    }
}