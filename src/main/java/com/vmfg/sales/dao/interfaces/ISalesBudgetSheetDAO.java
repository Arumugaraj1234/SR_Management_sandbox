package com.vmfg.sales.dao.interfaces;

import java.util.List;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.sales.entity.BudgetListEntity;
import com.vmfg.sales.request.DeleteBudgetSheetRequest;
import com.vmfg.sales.request.DeleteBudgetValue;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;
import com.vmfg.sales.request.getFileConfigDtlRequest;

public interface ISalesBudgetSheetDAO {

	ResponseAsList getSalesBudgetSheetHdrAndDtl(String masterId, String tenantId, String isBudgetFlag);

	ResponseAsList getKeyCategory(getFileConfigDtlRequest request);

	ResponseAsMessage deleteKeyCategory(DeleteBudgetValue request);

	ResponseAsMessage insertOrUpdateSalesBudgetSheetDtl(String masterId, String tenantId, String paymentTerms,
			String totalBudgetCost, String transactionStatus, String transactionStatusSeq, String sbHdrId,
			String sbDtlId, String keyCategory, String dtlSbHdrId, String dtlTenantId, String dtlValue);

	String insertOrUpdateSalesBudgetSheetHdr(String masterId, String tenantId, String paymentTerms,
			String totalBudgetCost, String transactionStatus, String transactionStatusSeq, String sbHdrId);

	int uploadBudgetSheetTemplate(BudgetListEntity budgetSheetFileEntity);
	
	List<SalesBudgetSheetExntDtlEntity> getcriticalListByPmHdrId(String pmHdrId,String tenantId);

	int deleteBudgetSheetCR(DeleteBudgetSheetRequest deleteBudgetSheetRequest);

}
