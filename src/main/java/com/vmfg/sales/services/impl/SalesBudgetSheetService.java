package com.vmfg.sales.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.sales.dao.interfaces.ISalesBudgetSheetDAO;
import com.vmfg.sales.entity.BudgetListEntity;
import com.vmfg.sales.request.DeleteBudgetSheetRequest;
import com.vmfg.sales.request.DeleteBudgetValue;
import com.vmfg.sales.request.SalesBudgetSheetDtlRequest;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;
import com.vmfg.sales.request.SalesBudgetSheetHdrAndDtlRequest;
import com.vmfg.sales.request.SalesBudgetSheetRequest;
import com.vmfg.sales.request.getFileConfigDtlRequest;
import com.vmfg.sales.services.interfaces.ISalesBudgetSheetService;

@Service
public class SalesBudgetSheetService implements ISalesBudgetSheetService {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetService.class);

	@Autowired
	private ISalesBudgetSheetDAO iSalesBudgetSheetDAO;

	

	@Override
	public ResponseAsList getSalesBudgetSheetHdrAndDtl(SalesBudgetSheetRequest salesBudgetSheetRequest) {
		ResponseAsList list = null;

		try {
			String masterId = salesBudgetSheetRequest.getMasterId();
			String tenantId = salesBudgetSheetRequest.getTenantId();
			String isBudgetFlag = salesBudgetSheetRequest.getIsBudget();
			

			list = iSalesBudgetSheetDAO.getSalesBudgetSheetHdrAndDtl(masterId, tenantId,isBudgetFlag);

		} catch (Exception ex) {
			logger.error("getSalesBudgetSheetHdrAndDtl Error " + ex);
		}
		return list;
	}

	@Override
	public ResponseAsMessage insertOrUpdateSalesBudgetSheetHdrAndDtl(
			List<SalesBudgetSheetHdrAndDtlRequest> salesBudgetSheetRequest) {

		ResponseAsMessage responseMsg = null;
		try {
			if (salesBudgetSheetRequest.size() > 0) {
				String masterId = salesBudgetSheetRequest.get(0).getMasterId();
				String tenantId = salesBudgetSheetRequest.get(0).getTenantId();
				String paymentTerms = salesBudgetSheetRequest.get(0).getPaymentTerms();
				String totalBudgetCost = salesBudgetSheetRequest.get(0).getTotalBudgetCost();
				String transactionStatus = salesBudgetSheetRequest.get(0).getTransactionStatus();
				String transactionStatusSeq = salesBudgetSheetRequest.get(0).getTransactionStatusSeq();
				String sbHdrId = salesBudgetSheetRequest.get(0).getSbHdrId();

				sbHdrId = iSalesBudgetSheetDAO.insertOrUpdateSalesBudgetSheetHdr(masterId, tenantId, paymentTerms,
						totalBudgetCost, transactionStatusSeq, transactionStatusSeq, sbHdrId);
				for (int g = 0; g < salesBudgetSheetRequest.size(); g++) {

					List<SalesBudgetSheetDtlRequest> salHdr = salesBudgetSheetRequest.get(g).getSalesDtlList();

					for (int h = 0; h < salHdr.size(); h++) {
						String sbDtlId = salHdr.get(h).getSbDtlId();
						String keyCategory = salHdr.get(h).getKeyCategory();
						String dtlSbHdrId = sbHdrId;
						String dtlTenantId = salHdr.get(h).getTenantId();
						String dtlValue = salHdr.get(h).getValue();

						responseMsg = iSalesBudgetSheetDAO.insertOrUpdateSalesBudgetSheetDtl(masterId, tenantId,
								paymentTerms, totalBudgetCost, transactionStatus, transactionStatusSeq, sbHdrId,
								sbDtlId, keyCategory, dtlSbHdrId, dtlTenantId, dtlValue);
					}

				}

			}

		} catch (Exception ex) {
			logger.error("insertOrUpdateSalesBudgetSheetHdrAndDtl Error " + ex);
		}
		return responseMsg;
	}

	@Override
	public ResponseAsList getKeyCategory(getFileConfigDtlRequest request) {
		return iSalesBudgetSheetDAO.getKeyCategory(request);
	}

	@Override
	public ResponseAsMessage deleteKeyCategory(DeleteBudgetValue request) {
		return iSalesBudgetSheetDAO.deleteKeyCategory(request);
	}

	@Override
	public ResponseAsMessage uploadBudgetSheetTemplate(BudgetListEntity budgetSheetFileEntity) {
		ResponseAsMessage returnMsg = new ResponseAsMessage();
		int returnData = 0;

		try {
			returnData = iSalesBudgetSheetDAO.uploadBudgetSheetTemplate(budgetSheetFileEntity);

			if (returnData > 0) {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseMessage(ResponseMessageMap.success);

			} else {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnMsg.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("uploadBudgetSheetTemplate service error " + ex);
		}
		return returnMsg;
	}

	@Override
	public ResponseAsList getcriticalListByPmHdrId(SalesBudgetSheetRequest salesBudgetSheetRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<SalesBudgetSheetExntDtlEntity> saleBudgetList = new ArrayList<SalesBudgetSheetExntDtlEntity>();
		try {
			
			saleBudgetList = iSalesBudgetSheetDAO.getcriticalListByPmHdrId(salesBudgetSheetRequest.getMasterId(), salesBudgetSheetRequest.getTenantId());
			if (saleBudgetList.size() > 0) {
				returnList.setResponseData(saleBudgetList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(saleBudgetList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getSalesBudgetSheetHdrAndDtl Error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage deleteBudgetSheetCR(DeleteBudgetSheetRequest deleteBudgetSheetRequest) {
		
		int returnData = 0;
		ResponseAsMessage returnMsg = new ResponseAsMessage();

		try {
			returnData = iSalesBudgetSheetDAO.deleteBudgetSheetCR(deleteBudgetSheetRequest);

			if (returnData == 1) {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnMsg.setResponseMessage(ResponseMessageMap.success);

			}else if(returnData == 2) {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnMsg.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
			}else {
				returnMsg.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnMsg.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("deleteBudgetSheetCR service error " + ex);
		}
		return returnMsg;
	}

}
