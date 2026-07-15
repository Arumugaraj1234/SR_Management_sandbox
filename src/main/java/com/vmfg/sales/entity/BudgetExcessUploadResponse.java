package com.vmfg.sales.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class BudgetExcessUploadResponse {
 List<BudgetSheetFileEntity> list=new ArrayList<BudgetSheetFileEntity>();
 List<String> errorMsgs = new ArrayList<>();
}
