-- NEW-flow Budget Excess Sheet redesign: freezes the station's Allocated Value and
-- Actual Spent So Far at the moment a Budget Excess is raised, so ACTUAL_EXCESS can be
-- computed against real station budget instead of the always-zero legacy TARGET_VALUE.
-- Legacy rows leave both columns NULL.
ALTER TABLE budget_excess_dtl
    ADD COLUMN ALLOCATED_VALUE DECIMAL(12,2) NULL AFTER ACTUAL_EXCESS,
    ADD COLUMN ACTUAL_SPENT_SO_FAR DECIMAL(12,2) NULL AFTER ALLOCATED_VALUE;
