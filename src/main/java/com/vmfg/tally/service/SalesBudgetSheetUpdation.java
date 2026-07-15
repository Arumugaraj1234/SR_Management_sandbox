package com.vmfg.tally.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SalesBudgetSheetUpdation {

    private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetUpdation.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Scheduler will run daily at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void updateReceivedBalance() {
        try {
            // 1. Read all receivable_billing_dtl entries
            String fetchQry =
                    "SELECT rbd.PROJECT_CODE, rbd.RECEIVABLE_OPEN_BAL, rbd.RECEIVABLE_PENDING_BAL " +
                            "FROM receivable_billing_dtl rbd " +
                            "JOIN ( " +
                            "    SELECT PROJECT_CODE, MAX(CREATED_DATETIME) AS max_created " +
                            "    FROM receivable_billing_dtl " +
                            "    GROUP BY PROJECT_CODE " +
                            ") t ON rbd.PROJECT_CODE = t.PROJECT_CODE AND rbd.CREATED_DATETIME = t.max_created";
            List<Map<String, Object>> receivables = jdbcTemplate.queryForList(fetchQry);

            logger.info("Found {} receivable billing records", receivables.size());

            // 2. Iterate over each record
            for (Map<String, Object> row : receivables) {
                String projectCode = (String) row.get("PROJECT_CODE");
                BigDecimal openingBal = row.get("RECEIVABLE_OPEN_BAL") != null
                        ? new BigDecimal(row.get("RECEIVABLE_OPEN_BAL").toString()) : BigDecimal.ZERO;
                BigDecimal pendingBal = row.get("RECEIVABLE_PENDING_BAL") != null
                        ? new BigDecimal(row.get("RECEIVABLE_PENDING_BAL").toString()) : BigDecimal.ZERO;

                BigDecimal receivedBal = openingBal.subtract(pendingBal);

                // 3. Update sales_budget_sheet_hdr using joins
                String updateQry =
                        "UPDATE sales_budget_sheet_hdr sb " +
                                "JOIN sales_enq_hdr se ON sb.MASTER_ID = se.SE_ID " +
                                "JOIN project_hdr ph ON se.SE_ID = ph.ENQUIRY_ID " +
                                "SET sb.RECEIVED_BALANCE = ? " +
                                "WHERE ph.PROJECT_CODE = ?";
                int rows = jdbcTemplate.update(updateQry, receivedBal, projectCode);

                logger.info("Project {} updated with RECIEVED_BALANCE = {} (rows affected: {})",
                        projectCode, receivedBal, rows);
            }

            logger.info("Receivable update completed successfully.");

        } catch (Exception e) {
            logger.error("Error while updating RECIEVED_BALANCE: {}", e.getMessage(), e);
        }
    }
}

