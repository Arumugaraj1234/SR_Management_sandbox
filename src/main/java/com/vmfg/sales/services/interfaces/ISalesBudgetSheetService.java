package com.vmfg.sales.services.interfaces;

import java.util.List;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.sales.entity.BudgetListEntity;
import com.vmfg.sales.request.DeleteBudgetSheetRequest;
import com.vmfg.sales.request.DeleteBudgetValue;
import com.vmfg.sales.request.SalesBudgetSheetHdrAndDtlRequest;
import com.vmfg.sales.request.SalesBudgetSheetRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;

public interface ISalesBudgetSheetService {

	ResponseAsList getSalesBudgetSheetHdrAndDtl(SalesBudgetSheetRequest salesBudgetSheetRequest);

	ResponseAsMessage insertOrUpdateSalesBudgetSheetHdrAndDtl(
			List<SalesBudgetSheetHdrAndDtlRequest> salesBudgetSheetRequest);

	ResponseAsList getKeyCategory(getFileConfigDtlRequest request);

	ResponseAsMessage deleteKeyCategory(DeleteBudgetValue request);

	ResponseAsMessage uploadBudgetSheetTemplate(BudgetListEntity uploadIndentReq);

	ResponseAsList getcriticalListByPmHdrId(SalesBudgetSheetRequest salesBudgetSheetRequest);

	ResponseAsMessage deleteBudgetSheetCR(DeleteBudgetSheetRequest deleteBudgetSheetRequest);
}
