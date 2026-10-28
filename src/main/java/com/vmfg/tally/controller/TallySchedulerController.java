package com.vmfg.tally.controller;

import com.vmfg.tally.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.ws.Response;

@Controller
public class TallySchedulerController {

    @Autowired
    private ReceivableSyncService receivableSyncService;

    @Autowired
    private PayableSyncService payableSyncService;

    @Autowired
    private PraUpdateService praUpdateService;

    @Autowired
    private SalesBudgetSheetUpdation salesBudgetSheetUpdation;

    @Autowired
    private PaymentSyncService paymentSyncService;

    @Autowired
    private ReceiptSyncService receiptSyncService;

    @Autowired
    private TransactionSyncService transactionSyncService;

    // Tally Transactions.json -> tally_voucher_hdr/dtl, then PRA amount due/status (PraUpdateService)
    @Scheduled(cron = "0 30 8 * * *") // Daily at 8:30 AM
    public void triggerTransactionSync() {
        transactionSyncService.syncTransactionsFromJson();
    }

//    @Scheduled(cron = "0 30 8 * * *") // Daily at 8:30 AM
    public void triggerReceivableSync() {
        receivableSyncService.syncReceivablesFromJson();
    }
//    @Scheduled(cron = "0 30 8 * * *") // Daily at 8:30 AM
    public void triggerPayableSync() {
        payableSyncService.syncPayablesFromJson();
    }

    // Replaced by triggerTransactionSync, which updates the PRAs right after the Tally sync
//    @Scheduled(cron = "0 0 9 * * ?")
    public void updatePraAfterScheduler(){
        praUpdateService.updatePraCompletionStatus();
    }

    // Scheduler will run daily at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void updateSalesBudgetSheetAfterUpdation(){
        salesBudgetSheetUpdation.updateReceivedBalance();
    }

    public void triggerPraUpdate(){
        praUpdateService.updatePraCompletionStatus();
    }

    @GetMapping("/triggerRecievable")
    public ResponseEntity<String> triggerNow() {
        receivableSyncService.syncReceivablesFromJson();
        return ResponseEntity.ok("Sync triggered manually");
    }
    @GetMapping("/triggerPayable")
    public ResponseEntity<String> triggerPayable(){
        payableSyncService.syncPayablesFromJson();
        return ResponseEntity.ok("Manual sync for payable completed");
    }

    @GetMapping("/triggerPraUpdateService")
    public ResponseEntity<String> triggerPraUpdateService(){
        praUpdateService.updatePraCompletionStatus();
        return ResponseEntity.ok("Manual sync for PRA updation completed");
    }

    @GetMapping("/triggerSalesUpdateSevice")
    public ResponseEntity<String> triggerSalesUpdateService(){
        salesBudgetSheetUpdation.updateReceivedBalance();
        return ResponseEntity.ok("Manual sync for Sales updation completed");
    }

    @GetMapping("/triggerPaymentExport")
    public ResponseEntity<String> triggerPaymentExport(){
        paymentSyncService.syncPaymentsFromJson();
        return ResponseEntity.ok("Manual sync for Payment export completed");
    }

    @GetMapping("/triggerTransactionSync")
    public ResponseEntity<String> triggerTransactionSyncNow(){
        transactionSyncService.syncTransactionsFromJson();
        return ResponseEntity.ok("Manual sync for Tally transactions completed");
    }

    @GetMapping("/triggerReceiptExport")
    public ResponseEntity<String> triggerReceiptExport(){
        receiptSyncService.syncReceiptsFromJson();
        return ResponseEntity.ok("Manual sync for Receipt export completed");
    }
}
 