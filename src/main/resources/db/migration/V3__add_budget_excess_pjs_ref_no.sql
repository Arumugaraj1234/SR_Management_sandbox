-- Client-facing reference code for a raised Budget Excess, format {PROJECT_CODE}/{DISCIPLINE}/PJS/{SEQ}
-- e.g. 1096/E/PJS/1. Minted once at raise-time (see IndentGroupService.raiseBudgetExcess /
-- BudgetExcessSheetService.insertBudgetExcessSheetDtl) and never recomputed, same as INDENT_CODE/PO_CODE.
-- Legacy rows leave this NULL.
ALTER TABLE budget_excess_dtl
    ADD COLUMN PJS_REF_NO VARCHAR(32) NULL AFTER ACTUAL_SPENT_SO_FAR;
