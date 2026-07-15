package com.vmfg.assembly.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.dao.interfaces.IAssemblyIssueDAO;
import com.vmfg.assembly.entity.MaterialIssueDtlEntity;
import com.vmfg.assembly.entity.MaterialIssueHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockIssueEntity;
import com.vmfg.assembly.request.InsertMaterialIssueDtlRequest;
import com.vmfg.assembly.request.InsertMaterialIssueRequest;
import com.vmfg.assembly.request.MaterialIssueHdrRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyIssueService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.scm.dao.impl.PoDAO;

@Service
public class AssemblyIssueService implements IAssemblyIssueService {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyIssueService.class);
	@Autowired
	private IAssemblyIssueDAO iAssemblyIssueDAO;
	
	@Autowired
	PoDAO poDAO;

	@Override
	public ResponseAsMessage insertMaterialIssueHdrAndDtl(InsertMaterialIssueRequest insertMrDtls) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertMaterialIssueHdrAndDtl Service start ");
		int responseMiHdrId = 0, responseMiDtlId = 0;
		try {
			responseMiHdrId = iAssemblyIssueDAO.insertMaterialIssueHdr(insertMrDtls.getPmHdrId(),
					insertMrDtls.getMrHdrId(), insertMrDtls.getIssuedBy(),
					insertMrDtls.getRemarks(), insertMrDtls.getTenantId());

			if (responseMiHdrId > 0) {
				String miCode= iAssemblyIssueDAO.getMtlIusseCode(Integer.toString(responseMiHdrId) );
				for (InsertMaterialIssueDtlRequest dtlObj : insertMrDtls.getMiDtlList()) {
			String productCode = poDAO.getProdCodeByprodId(dtlObj.getProductId());
		//	String projectId = poDAO.getPmHdrIdByIndentDtlId(dtlObj.getProductId());
			String inventoryLoctionCode = iAssemblyIssueDAO.getInventoryLoctionCodeByMrDtlId(dtlObj.getMrDtlId());
			
					responseMiDtlId = iAssemblyIssueDAO.insertMaterialIssueDtl(responseMiHdrId, dtlObj.getMrDtlId(),
							dtlObj.getProductId(), dtlObj.getRequestedQty(), dtlObj.getAvailableQty(),
							dtlObj.getIssuedQty(), dtlObj.getTenantId(),productCode,insertMrDtls.getPmHdrId(),miCode,insertMrDtls.getIssuedBy(),inventoryLoctionCode);
					
					iAssemblyIssueDAO.updateMRDtlIssueQty(dtlObj.getMrDtlId(), dtlObj.getIssuedQty());
					}
					int mrHdrStatusCount=iAssemblyIssueDAO.getMRCompletedStatus(insertMrDtls.getMrHdrId());
					if(mrHdrStatusCount==0) {
						iAssemblyIssueDAO.updateMrHdrCompletedStatus(insertMrDtls.getMrHdrId(), insertMrDtls.getEmpId());
					}
				}
			
			if (responseMiHdrId > 0 && responseMiDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.mrIssued);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertMaterialIssueHdrAndDtl Service end ");
		} catch (Exception ex) {
			logger.error("insertMaterialIssueHdrAndDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList getMaterialIssueHdr(MaterialIssueHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialIssueHdrEntity> list = new ArrayList<MaterialIssueHdrEntity>();
		logger.info("getMaterialIssueHdr Service start ");
		try {
			list = iAssemblyIssueDAO.getMaterialIssueHdr(materialHdrReq.getHdrId(), materialHdrReq.getTenantId(),materialHdrReq.getProductId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialIssueHdr Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialIssueHdr error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getMaterialIssueDtl(MaterialIssueHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialIssueDtlEntity> list = new ArrayList<MaterialIssueDtlEntity>();
		logger.info("getMaterialIssueDtl Service start ");
		try {
			list = iAssemblyIssueDAO.getMaterialIssueDtl(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialIssueDtl Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialIssueDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList retriveFromIssueStock(MaterialIssueHdrRequest retriveFromStock) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetriveFromStockIssueEntity> list = new ArrayList<>();
		logger.info("retriveFromIssueStock Service start ");
		try {
			list = iAssemblyIssueDAO.retriveFromIssueStock(retriveFromStock.getHdrId(), retriveFromStock.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("retriveFromIssueStock Service end ");
		} catch (Exception ex) {
			logger.error("retriveFromIssueStock error " + ex);
		}
		return returnList;
	}

}
