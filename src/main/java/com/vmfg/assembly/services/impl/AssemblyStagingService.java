package com.vmfg.assembly.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.dao.interfaces.IAssemblyStagingDAO;
import com.vmfg.assembly.entity.MsHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveForMSEntity;
import com.vmfg.assembly.entity.RetrieveMSDtlByHdrEntity;
import com.vmfg.assembly.request.InsertMsDtlRequest;
import com.vmfg.assembly.request.InsertMsHdrAndDtlRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyStagingService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;

@Service
public class AssemblyStagingService implements IAssemblyStagingService {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyStagingService.class);

	@Autowired
	private IAssemblyStagingDAO iAssemblyStagingDAO;

	@Override
	public ResponseAsList msHdrRetrieve(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MsHdrRetrieveEntity> list = new ArrayList<MsHdrRetrieveEntity>();
		logger.info("msHdrRetrieve Service start ");
		try {
			list = iAssemblyStagingDAO.msHdrRetrieve(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("msHdrRetrieve Service end ");
		} catch (Exception ex) {
			logger.error("msHdrRetrieve error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList msHdrRetrieveAll(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MsHdrRetrieveEntity> list = new ArrayList<MsHdrRetrieveEntity>();
		logger.info("msHdrRetrieveAll Service start ");
		try {
			list = iAssemblyStagingDAO.msHdrRetrieveAll(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("msHdrRetrieveAll Service end ");
		} catch (Exception ex) {
			logger.error("msHdrRetrieveAll error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList retrieveMSDtlByHdr(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetrieveMSDtlByHdrEntity> list = new ArrayList<>();
		logger.info("retrieveMSDtlByHdr Service start ");
		try {
			list = iAssemblyStagingDAO.retrieveMSDtlByHdr(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("retrieveMSDtlByHdr Service end ");
		} catch (Exception ex) {
			logger.error("retrieveMSDtlByHdr error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertMsHdrAndDtl(InsertMsHdrAndDtlRequest insertMsDtls) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertMsHdrAndDtl Service start ");
		int responseMsHdrId = 0, responseMsDtlId = 0;

		try {
			int existingCount = iAssemblyStagingDAO.checkMsHdrNameExists(insertMsDtls.getPmHdrId(),
					insertMsDtls.getMsName(), insertMsDtls.getTenantId());

			if (existingCount > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseAlreadyExists);
				returnres.setResponseMessage(ResponseMessageMap.msNameDuplicate);
				return returnres;
			}

			responseMsHdrId = iAssemblyStagingDAO.insertMsHdr(insertMsDtls.getPmHdrId(), insertMsDtls.getMsName(),
					insertMsDtls.getStageQty(), insertMsDtls.getTenantId(), insertMsDtls.getCreatedBy(),
					insertMsDtls.getUom());

			if (responseMsHdrId > 0) {
				for (InsertMsDtlRequest dtlObj : insertMsDtls.getMsDtlList()) {

					responseMsDtlId = iAssemblyStagingDAO.insertMsDtl(responseMsHdrId, dtlObj.getProductId(),
							dtlObj.getQty(), dtlObj.getTenantId(), insertMsDtls.getPmHdrId(),
							insertMsDtls.getCreatedBy(), insertMsDtls.getMsName());
				}
			}
			if (responseMsHdrId > 0 && responseMsDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertMsHdrAndDtl Service end ");
		} catch (Exception ex) {
			logger.error("insertMsHdrAndDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage cancelMsHdrReq(MaterialReqHdrRequest materialReqHdr) {
		return finalizeMsHdr(materialReqHdr, false);
	}

	@Override
	public ResponseAsMessage useMsHdrForReturn(MaterialReqHdrRequest materialReqHdr) {
		return finalizeMsHdr(materialReqHdr, true);
	}

	private ResponseAsMessage finalizeMsHdr(MaterialReqHdrRequest materialReqHdr, boolean usedForReturn) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		String logTag = usedForReturn ? "useMsHdrForReturn" : "cancelMsHdrReq";
		logger.info(logTag + " Service start ");
		int responseMsHdrId = 0;
		try {

			List<MsHdrRetrieveEntity> list = iAssemblyStagingDAO.msHdrRetrieveByHdrId(materialReqHdr.getHdrId(),
					materialReqHdr.getTenantId());

			if (list.size() > 0) {
				List<RetrieveMSDtlByHdrEntity> list1 = iAssemblyStagingDAO
						.retrieveMSDtlByHdr(list.get(0).getMsHdrId(), materialReqHdr.getTenantId());

				if (list1.size() > 0) {

					for (int b = 0; b < list1.size(); b++) {
						iAssemblyStagingDAO.updateMsDtlInv(list1.get(b).getProductId(), list1.get(b).getQty(),
								list.get(0).getPmHdrId(), materialReqHdr.getTenantId(), materialReqHdr.getEmpId(),
								list1.get(b).getDtlId(), list.get(0).getMsHdrId(), list.get(0).getMsName());
					}

				}

				String prodCode = iAssemblyStagingDAO.prodIdFromDesc(list.get(0).getMsName(),
						list.get(0).getPmHdrId(), materialReqHdr.getTenantId());

				if (usedForReturn) {
					responseMsHdrId = iAssemblyStagingDAO.useMsHdrForReturn(materialReqHdr.getHdrId(),
							materialReqHdr.getTenantId(), materialReqHdr.getEmpId(), list.get(0).getStageQty(),
							list.get(0).getPmHdrId(), prodCode);
				} else {
					responseMsHdrId = iAssemblyStagingDAO.cancelMsHdrReq(materialReqHdr.getHdrId(),
							materialReqHdr.getTenantId(), materialReqHdr.getEmpId(), list.get(0).getStageQty(),
							list.get(0).getPmHdrId(), prodCode);
				}

				if (responseMsHdrId > 0) {
					returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnres.setResponseMessage(ResponseMessageMap.successfulDeleted);
				} else {
					returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					returnres.setResponseMessage(ResponseMessageMap.deleteUnSuccessful);
				}
			}

			logger.info(logTag + " Service end ");
		} catch (Exception ex) {
			logger.error(logTag + " error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsList retrieveForMS(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetrieveForMSEntity> list = new ArrayList<>();
		logger.info("retrieveForMS Service start ");
		try {
			list = iAssemblyStagingDAO.retrieveForMS(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("retrieveForMS Service end ");
		} catch (Exception ex) {
			logger.error("retrieveForMS error " + ex);
		}
		return returnList;
	}

}
