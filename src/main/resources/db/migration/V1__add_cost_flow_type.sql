-- Tags each project as LEGACY (Budget/Target/Actual Cost on WBS screen) or NEW
-- (Real Cost from PO). Existing rows auto-backfill to LEGACY via the column default.
ALTER TABLE project_hdr
    ADD COLUMN COST_FLOW_TYPE VARCHAR(10) NOT NULL DEFAULT 'LEGACY' AFTER TENANT_ID;
