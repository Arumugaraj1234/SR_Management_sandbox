package com.vmfg.sales.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetSheetPaymentEntity {

        private String term;
        private String percentage;
        private String plannedDate;
        private String actualDate;
        private String remarks;                 
        private String sbPtId;
        
}	
