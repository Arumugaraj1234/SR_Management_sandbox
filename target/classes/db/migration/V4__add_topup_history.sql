-- Audit trail for station Topup / Allocate to Station (insertSubAreaExtn) - who allocated
-- how much, when, and from which screen (PJS or WBS). One append-only row per topup call;
-- Element/Element Desc/Specification/Make are resolved at read-time via SB_EXTN_ID join to
-- sales_budget_sheet_extn, same as the existing Budget Link table itself, since that source
-- row is never deleted (only its ALLOCATED_QTY/ALLOCATED_VALUE incremented).
CREATE TABLE project_key_area_extn_hist (
    PKSE_HIST_ID INT NOT NULL AUTO_INCREMENT,
    PKA_ID INT NULL,
    SB_EXTN_ID INT NULL,
    ALLOCATED_QTY DECIMAL(11,2) NULL DEFAULT 0.00,
    ALLOCATED_VALUE DECIMAL(11,2) NULL DEFAULT 0.00,
    SOURCE VARCHAR(10) NULL,
    CREATED_BY VARCHAR(45) NULL,
    CREATED_ON DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    TENANT_ID VARCHAR(45) NULL,
    PRIMARY KEY (PKSE_HIST_ID)
);
