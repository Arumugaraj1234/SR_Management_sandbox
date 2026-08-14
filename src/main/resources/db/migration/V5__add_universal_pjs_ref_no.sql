-- Universal PJS No., minted once per PJS at creation time (indent_grp_scs), format
-- {PROJECT_CODE}/{DISCIPLINE}/PJS/{SEQ} - same scheme budget_excess_dtl.PJS_REF_NO (V3)
-- already used, but that one only ever existed for a PJS that had a Budget Excess raised.
-- Sequence is project-wide (not per-discipline), matching the original PJS_REF_NO scheme.
-- See IndentGroupService.insertScpDtlsByIgHdrId (mint point) and
-- BudgetExcessSheetService.insertBudgetExcessSheetDtl (now reads this instead of minting
-- its own).
ALTER TABLE indent_grp_scs
    ADD COLUMN PJS_REF_NO VARCHAR(32) NULL AFTER CUSTOMER_APPROVAL;

-- Backfill existing PJS in creation order, project-wide sequence, per project. Rows whose
-- indent/project/SBC lookup doesn't resolve (broken pre-existing data - 19 out of 4028 rows
-- in the sandbox at time of writing) are left NULL rather than breaking the migration.
WITH resolvable AS (
    SELECT
        igs.IG_SCS_ID,
        ph.PROJECT_CODE,
        sbc.SHORT_DESC,
        ROW_NUMBER() OVER (PARTITION BY ih.PROJECT_ID ORDER BY igs.CREATED_DATETIME, igs.IG_SCS_ID) AS SEQ
    FROM indent_grp_scs igs
    JOIN indent_hdr ih ON ih.INDENT_ID = igs.INDENT_ID
    JOIN project_hdr ph ON ph.PM_HDR_ID = ih.PROJECT_ID
    JOIN sales_budget_category sbc ON sbc.SBC_CODE = ih.SBC_CODE AND sbc.TENANT_ID = igs.TENANT_ID
)
UPDATE indent_grp_scs igs
JOIN resolvable r ON r.IG_SCS_ID = igs.IG_SCS_ID
SET igs.PJS_REF_NO = CONCAT(r.PROJECT_CODE, '/', r.SHORT_DESC, '/PJS/', r.SEQ);

-- Point every existing raised-excess row at its PJS's own (now-backfilled) number instead
-- of the one it minted independently at raise-time off a different counter - one PJS No.
-- everywhere from here on, not two different ones depending which screen you look at. This
-- changes the historical PJS Ref No. shown on already-completed Budget Excess Sheet rows -
-- expected/deliberate per the unify decision, not a data-loss concern (old value is
-- superseded, not archived, since it was never anything other than a display label).
UPDATE budget_excess_dtl bed
JOIN indent_grp_scs igs ON igs.IG_SCS_ID = bed.IG_SCS_ID
SET bed.PJS_REF_NO = igs.PJS_REF_NO
WHERE igs.PJS_REF_NO IS NOT NULL;
