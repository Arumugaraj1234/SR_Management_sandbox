package com.vmfg.mis.services.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.IPMMisDAO;
import com.vmfg.mis.request.PMMileRequest;
import com.vmfg.mis.request.PMWidgetRequest;
import com.vmfg.mis.response.PMWorkLoadResponse;
import com.vmfg.mis.response.ReportProjectMilestoneResponse;
import com.vmfg.mis.response.ReportProjectTrackerResponse;
import com.vmfg.mis.services.interfaces.IPMMisService;

@Service
public class PMMisService implements IPMMisService {
	private static final Logger logger = LoggerFactory.getLogger(PMMisService.class);

	@Autowired
	IPMMisDAO iPMMisDAO;

	@Override
	public ResponseAsList getPMWidgetDtl(PMWidgetRequest pMWidgetReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<ReportProjectTrackerResponse> resp = null;
			if (pMWidgetReq.getPmHdrId().equalsIgnoreCase("getall")) {
				resp = iPMMisDAO.getAllPMDtlByDate(pMWidgetReq.getFromDate(), pMWidgetReq.getEndDate(),
						pMWidgetReq.getTenantId(), pMWidgetReq.getSbcCode());
			} else {
				resp = iPMMisDAO.getPMDtlByPmId(pMWidgetReq.getTenantId(), pMWidgetReq.getPmHdrId(),
						pMWidgetReq.getSbcCode());
			}

			if (resp.size() > 0) {

				BigDecimal balanceVal = BigDecimal.ZERO;
				
				balanceVal = new BigDecimal(resp.get(0).getPmBudgetValue())
					    .subtract(
					    		 new BigDecimal(resp.get(0).getScmPoValue())
//					            .subtract(new BigDecimal(resp.get(0).getMaterialTransferValue()))
//					            .subtract(new BigDecimal(resp.get(0).getEmpCostValue()))
					    );
				
				resp.get(0).setBalanceValue(String.valueOf(balanceVal));
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getPMWidgetDtl method exception " + ex.getMessage());
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPMBySBCType(PMWidgetRequest pMWidgetReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<ReportProjectTrackerResponse> resp = null;
			if (pMWidgetReq.getSbcCode().equalsIgnoreCase("getall")) {
				resp = iPMMisDAO.getPMBySBCTypeAll(pMWidgetReq.getFromDate(), pMWidgetReq.getEndDate(),
						pMWidgetReq.getTenantId(), pMWidgetReq.getPmHdrId());
			} else {
				resp = iPMMisDAO.getPMBySBCType(pMWidgetReq.getFromDate(), pMWidgetReq.getEndDate(),
						pMWidgetReq.getTenantId(), pMWidgetReq.getSbcCode(), pMWidgetReq.getPmHdrId());
			}

			if (resp.size() > 0) {

				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getPMBySBCType method exception " + ex.getMessage());
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPMMilestoneByMonthYr(PMMileRequest pMMileReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<ReportProjectMilestoneResponse> resp = null;

			resp = iPMMisDAO.getPMMilestoneByMonthYr(pMMileReq.getMonth(), pMMileReq.getYr(), pMMileReq.getTenantId(),
					pMMileReq.getPmHdrId());
			for (int g = 0; g < resp.size(); g++) {
				int cnt = iPMMisDAO.MileCountCheck(resp.get(g).getDepartment(), resp.get(g).getPmHdrId(),
						pMMileReq.getTenantId());

				if (cnt > 0) {
					resp.get(g).setActualEndDate(iPMMisDAO.getMileStoneEndDate(resp.get(g).getDepartment(),
							resp.get(g).getPmHdrId(), pMMileReq.getTenantId()));
				} else {
					resp.get(g).setActualEndDate("NA");
				}
			}

			if (resp.size() > 0) {

				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getPMBySBCType method exception " + ex.getMessage());
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPMReportTracker(PMWidgetRequest pMWidgetReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<ReportProjectTrackerResponse> resp = null;
			if (pMWidgetReq.getSbcCode().equalsIgnoreCase("getall")) {
				resp = iPMMisDAO.getPMBySBCTypeAllwithoutDate(pMWidgetReq.getTenantId(), pMWidgetReq.getPmHdrId());
			} else {
				resp = iPMMisDAO.getPMBySBCTypewithoutDate(pMWidgetReq.getTenantId(), pMWidgetReq.getSbcCode(),
						pMWidgetReq.getPmHdrId());
			}

			if (resp.size() > 0) {
				for (int i = 0; i < resp.size(); i++) {
					BigDecimal balanceVal = BigDecimal.ZERO;
					
					balanceVal = new BigDecimal(resp.get(i).getSaleValue())
						    .subtract(
						    		 new BigDecimal(resp.get(i).getScmPoValue())
						            .subtract(new BigDecimal(resp.get(i).getMaterialTransferValue()))
						            .subtract(new BigDecimal(resp.get(i).getEmpCostValue()))
						    );
					resp.get(i).setBalanceValue(String.valueOf(balanceVal));
				}
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getPMBySBCType method exception " + ex.getMessage());
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPMWorkLoad(PMWidgetRequest pMWidgetReq) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<PMWorkLoadResponse> resp = null;

			resp = iPMMisDAO.getPMWorkLoad(pMWidgetReq.getTenantId(), pMWidgetReq.getPmHdrId());

			if (resp.size() > 0) {

				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(resp);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception ex) {
			logger.error("getPMBySBCType method exception " + ex.getMessage());
		}
		return returnList;
	}

}
