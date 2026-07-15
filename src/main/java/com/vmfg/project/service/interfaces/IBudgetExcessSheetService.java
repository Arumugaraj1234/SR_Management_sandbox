package com.vmfg.project.service.interfaces;

import java.util.List;

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.entity.BudgetExcessSheetEntity;
import com.vmfg.project.entity.SalesCategoryDtlEntity;
import com.vmfg.project.request.BudgetExcessSheetRequest;
import com.vmfg.project.request.BudgetExcessStatusDtlReq;
import com.vmfg.project.request.IndentBudgetDtlReq;
import com.vmfg.project.request.updateBudgetExcessSheetRequest;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;

public interface IBudgetExcessSheetService {

	ResponseAsMessage insertBudgetExcessSheetDtl(BudgetExcessSheetRequest budgetExcessSheetRequest);

	ResponseAsMessage updateBudgetExcessSheetDtl(updateBudgetExcessSheetRequest updateudget);

	List<BudgetExcessSheetEntity> retriveBudgetExcessSheetDtl(BudgetExcessStatusDtlReq statusDtlReq);

	List<SalesCategoryDtlEntity> getIndentBudgetDtl(IndentBudgetDtlReq indentBudgetDtl);

	ResponseAsMessage updateBudgetSheetExcessSeqAndStatus(UpdateSeqAndStatusRequest updatePoDtlsEntity);

}
