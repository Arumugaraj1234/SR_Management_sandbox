package com.vmfg.assembly.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.assembly.dao.interfaces.IAssemblyDAO;
import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.assembly.entity.MaterialReqDtlEntity;
import com.vmfg.assembly.entity.MaterialReqHdrEntity;
import com.vmfg.assembly.entity.RetriveFromStockEntity;
import com.vmfg.assembly.request.GetAssyDtlRequest;
import com.vmfg.assembly.request.InsertMrDtlRequest;
import com.vmfg.assembly.request.InsertMrHdrAndDtlRequest;
import com.vmfg.assembly.request.IsStagingRequest;
import com.vmfg.assembly.request.MaterialReqHdrRequest;
import com.vmfg.assembly.request.RetriveFromStockRequest;
import com.vmfg.assembly.services.interfaces.IAssemblyService;
import com.vmfg.design.services.impl.ChangeRequestService;
import com.vmfg.export.request.DcReqDtlRequest;
import com.vmfg.export.request.DcRequestHdrRequest;
import com.vmfg.export.services.impl.DeliveryChallanReportService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;

@Service
public class AssemblyService implements IAssemblyService {
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestService.class);

	@Autowired
	private IAssemblyDAO iAssyDAO;
	
	@Autowired
	private DeliveryChallanReportService deliveryChallanReportService;

	@Override
	public ResponseAsList getAssyDtl(GetAssyDtlRequest getAssyDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		logger.info("getAssyDtl Service start ");
		try {
			List<GetAssyDtlEntity> getAssyDtl = iAssyDAO.getAssyDtl(getAssyDtlReq.getFromDate(),
					getAssyDtlReq.getToDate(), getAssyDtlReq.getCustName(), getAssyDtlReq.getAssyId(),
					getAssyDtlReq.getTenantID(), getAssyDtlReq.getPmId(), getAssyDtlReq.getEmpId(),getAssyDtlReq.getProjectId());
			for (int i = 0; i < getAssyDtl.size(); i++) {
				getAssyDtl.get(i)
						.setIndentCount(Integer.toString(iAssyDAO.getindentcount(getAssyDtl.get(i).getPmHdrId(), "0")));
				getAssyDtl.get(i).setIndentIsCompletedCount(
						Integer.toString(iAssyDAO.getindentcount(getAssyDtl.get(i).getPmHdrId(), "1")));
				getAssyDtl.get(i).setMaterialRequestHdrCount(
						Integer.toString(iAssyDAO.getMaterialReqHdrCount(getAssyDtl.get(i).getPmHdrId(), "0")));
				getAssyDtl.get(i).setMaterialRequestIsCompletedCount(
						Integer.toString(iAssyDAO.getMaterialReqHdrCount(getAssyDtl.get(i).getPmHdrId(), "1")));
				getAssyDtl.get(i).setIsInternal(iAssyDAO.getIsInternalOrNot(getAssyDtl.get(i).getPmHdrId()));
			}
			if (getAssyDtl.size() > 0) {
				returnList.setResponseCode(ResponseMessageMap.success);
				returnList.setResponseMessage(ResponseMessageMap.responseCodeOk);
				returnList.setResponseData(getAssyDtl);
			} else {
				returnList.setResponseCode(ResponseMessageMap.noRecord);
				returnList.setResponseMessage(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseData(getAssyDtl);
			}
		} catch (Exception ex) {
			logger.error("getAssyDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getMaterialReqHdr(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialReqHdrEntity> list = new ArrayList<MaterialReqHdrEntity>();
		logger.info("getMaterialReqHdr Service start ");
		try {
			list = iAssyDAO.getMaterialReqHdr(materialHdrReq.getHdrId(), materialHdrReq.getTenantId(),materialHdrReq.getRequestType());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialReqHdr Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialReqHdr error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getMaterialReqDtl(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<MaterialReqDtlEntity> list = new ArrayList<MaterialReqDtlEntity>();
		logger.info("getMaterialReqDtl Service start ");
		try {
			list = iAssyDAO.getMaterialReqDtl(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("getMaterialReqDtl Service end ");
		} catch (Exception ex) {
			logger.error("getMaterialReqDtl error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage cancelMiRequestHdr(MaterialReqHdrRequest materialHdrReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {

			resp = iAssyDAO.cancelMiRequestHdr(materialHdrReq.getHdrId(), materialHdrReq.getTenantId());

		} catch (Exception ex) {
			logger.error("Error updateAssyMstResp " + ex);
		}

		return resp;
	}

	@Override
	public ResponseAsList retriveFromStock(RetriveFromStockRequest retriveFromStock) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetriveFromStockEntity> list = new ArrayList<RetriveFromStockEntity>();
		logger.info("retriveFromStock Service start ");
		try {
			list = iAssyDAO.retriveFromStock(retriveFromStock.getPmHdrId(), retriveFromStock.getPkaId(),
					retriveFromStock.getPskaId(), retriveFromStock.getTenantId());
			List<RetriveFromStockEntity> finalList = new ArrayList<RetriveFromStockEntity>();
			for(int i=0;i<list.size();i++) {
				logger.info("retriveFromStock mainList " + list.get(i).getProductId() + " " + list.get(i).getProductode() + " " + list.get(i).getInvLocationCode());
				BigDecimal grnQty=iAssyDAO.getGrnQty(retriveFromStock.getPmHdrId(), retriveFromStock.getTenantId(),list.get(i).getProductode(),
						list.get(i).getProductId(),list.get(i).getInvLocationCode(), list.get(i).getProductDesc(), list.get(i).getSpecification());
				BigDecimal mrReqQty = iAssyDAO.getActualAvailableQty(retriveFromStock.getPmHdrId(), retriveFromStock.getTenantId(), list.get(i).getProductId(),
						list.get(i).getInvLocationCode());
				BigDecimal availQty=grnQty.subtract(mrReqQty);
				logger.info("retriveFromStock Service start " + availQty + " " + list.get(i).getProductode());
				
				if(availQty.compareTo(BigDecimal.ZERO) >0) {
					list.get(i).setAvailableQty(null);
					list.get(i).setAvailableQty(availQty.toString());
					finalList.add(list.get(i));
					logger.info("retriveFromStock Service avail qty " + list.get(i).getAvailableQty() + " " + list.get(i).getProductode());
					logger.info("Product ID " + list.get(i).getProductId());
				}
			}
			if (list.size() > 0) {
				returnList.setResponseData(finalList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			logger.info("retriveFromStock Service end ");
		} catch (Exception ex) {
			logger.error("retriveFromStock error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage insertMrHdrAndDtl(InsertMrHdrAndDtlRequest insertMrDtls) {
		ResponseAsMessage returnres = new ResponseAsMessage();
		logger.info("insertMrHdrAndDtl Service start ");
		int responseMrHdrId = 0, responseMrDtlId = 0;
		DcRequestHdrRequest deliveryReq= new DcRequestHdrRequest();
		 List<DcReqDtlRequest> dcreqdtlList =new ArrayList<DcReqDtlRequest>();
		 
		try {
			responseMrHdrId = iAssyDAO.insertMrHdr(insertMrDtls.getPmHdrId(), insertMrDtls.getRequestedBy(),
					insertMrDtls.getRequestedFor(), insertMrDtls.getTenantId(),insertMrDtls.getRequestType());

			if (responseMrHdrId > 0) {
				for (InsertMrDtlRequest dtlObj : insertMrDtls.getMrDtlList()) {
					responseMrDtlId = iAssyDAO.insertMrDtl(responseMrHdrId, dtlObj.getPoductId(),
							dtlObj.getRequestedQty(), dtlObj.getAvailableQty(), dtlObj.getTenantId(),
							dtlObj.getInventoryLocation());
					
							DcReqDtlRequest deliveryDtlObj=new DcReqDtlRequest();
							deliveryDtlObj.setClosedQty("0");
							deliveryDtlObj.setDescofGoods(dtlObj.getDescOfGoods());
							deliveryDtlObj.setProductId(dtlObj.getPoductId());
							deliveryDtlObj.setQty(dtlObj.getRequestedQty());
							deliveryDtlObj.setTenantId(insertMrDtls.getTenantId());
							dcreqdtlList.add(deliveryDtlObj);
				}
				// DC insert
				if(insertMrDtls.getRequestType().equalsIgnoreCase("0")) {
					deliveryReq.setMrHdrId(String.valueOf(responseMrHdrId));
					deliveryReq.setRequestedBy(insertMrDtls.getRequestedBy());
					deliveryReq.setPmHdrId(insertMrDtls.getPmHdrId());
					deliveryReq.setRemarks("Requested");
					deliveryReq.setIsCompleted("0");
					deliveryReq.setTenantId(insertMrDtls.getTenantId());
					deliveryReq.setDcreqdtl(dcreqdtlList);
					deliveryChallanReportService.getinsertqcreq(deliveryReq);
					
				}
			}
			if (responseMrHdrId > 0 && responseMrDtlId > 0) {
				returnres.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnres.setResponseMessage(ResponseMessageMap.successCreated);
			} else {
				returnres.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnres.setResponseMessage(ResponseMessageMap.failToCreateMsg);
			}
			logger.info("insertMrHdrAndDtl Service end ");
		} catch (Exception ex) {
			logger.error("insertMrHdrAndDtl error " + ex);
		}
		return returnres;
	}

	@Override
	public ResponseAsMessage retriveAssyResp(MaterialReqHdrRequest assyMstRequest) {
		ResponseAsMessage resp = new ResponseAsMessage();
		try {

			resp = iAssyDAO.retriveAssyResp(assyMstRequest);

		} catch (Exception ex) {
			logger.error("Error updateAssyMstResp " + ex);
		}

		return resp;
	}

	@Override
	public ResponseAsMessage retriveIsStagingStatus(IsStagingRequest isStagingReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
		String isQc = isStagingReq.getIsQc();
		String hdrId = isStagingReq.getHdrId();
		String tenantId = isStagingReq.getTenantId();
		try {
			// isQc is 0 assy_hdrId 	
			if(isQc.equalsIgnoreCase("1")) {
			resp = iAssyDAO.IsStagingStatusForQuality(hdrId, tenantId);
			}else {
			resp = iAssyDAO.IsStagingStatusForAssy(hdrId, tenantId);
			}

		} catch (Exception ex) {
			logger.error("Error retriveIsStagingStatus " + ex);
		}

		return resp;
	}

	@Override
	public ResponseAsMessage updateIsStagingStatus(IsStagingRequest isStagingReq) {
		ResponseAsMessage resp = new ResponseAsMessage();
//		String isQc = isStagingReq.getIsQc();
		String hdrId = isStagingReq.getHdrId();
		String tenantId = isStagingReq.getTenantId();
		try {
			
			int isStatus = iAssyDAO.checkIsStagingStatus(hdrId, tenantId);
               if(isStatus == 3) {
            	   resp.setResponseCode(ResponseMessageMap.failToupdateCode);
   				   resp.setResponseMessage("This project is not assigned to asssembly team");
	        }
               else {
	         int updateStatus = iAssyDAO.updateIsStagingStatus(hdrId, isStatus, tenantId);
	        
			    if(updateStatus == 1) {
				   resp.setResponseCode(ResponseMessageMap.responseCodeOk);
				   resp.setResponseMessage(ResponseMessageMap.successUpdated);
			     }else {
			       resp.setResponseCode(ResponseMessageMap.failToupdateCode);
				   resp.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			     }
	         }
		} catch (Exception ex) {
			logger.error("Error updateIsStagingStatus " + ex);
		}

		return resp;
	}
}
