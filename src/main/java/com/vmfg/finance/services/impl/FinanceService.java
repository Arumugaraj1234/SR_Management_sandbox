package com.vmfg.finance.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.request.DesignRequest;
import com.vmfg.finance.dao.interfaces.IFinanceDAO;
import com.vmfg.finance.entity.FinanceHdrEntity;
import com.vmfg.finance.services.interfaces.IFinanceService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;

@Service
public class FinanceService implements IFinanceService {
	private static final Logger logger = LoggerFactory.getLogger(FinanceService.class);
	@Autowired
	IFinanceDAO iFinanceDAO;

	@Override
	public ResponseAsList getFinanceDtl(DesignRequest designReq) {
		ResponseAsList returnL = new ResponseAsList();
		try {
			List<FinanceHdrEntity> returnList = null;
			
			String financeId=designReq.getDesignID();
			returnList = iFinanceDAO.getFinanceDtl(designReq.getFromDate(), designReq.getToDate(),
					designReq.getCustomer(), designReq.getProcessId(), designReq.getEmpId(), designReq.getTenantID(),
					financeId,designReq.getProjectId());

			if (returnList.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(returnList);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("error in getFinanceDtl service " + ex.getMessage());
		}

		return returnL;

	}



}
