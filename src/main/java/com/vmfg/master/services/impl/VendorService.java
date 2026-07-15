package com.vmfg.master.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.master.dao.interfaces.IVendorDAO;
import com.vmfg.master.entity.CustomerMstEntity;
import com.vmfg.master.entity.VendorCategoryEntity;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.master.entity.VendorRatingEntity;
import com.vmfg.master.request.CustomerComplaintCheck;
import com.vmfg.master.request.InsertVendorReq;
import com.vmfg.master.request.VendorAllDtlReq;
import com.vmfg.master.request.VendorApprDtlReq;
import com.vmfg.master.request.VendorInspRatingRequest;
import com.vmfg.master.request.VendorRatingResponse;
import com.vmfg.master.services.interfaces.IVendorService;
import com.vmfg.quality.dao.interfaces.IQualityInspectionDAO;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.scm.dao.interfaces.IIndentGroupDAO;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class VendorService implements IVendorService{
	private static final Logger logger = LoggerFactory.getLogger(VendorService.class);
	
	@Autowired
	private IVendorDAO iVendorDAO;
	
	@Autowired
	private IndentUploadDAO indentUploadDAO;
	
	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;
	
	@Autowired
	private IIndentGroupDAO iIndentGroupDAO;
	
	@Autowired
	private IQualityInspectionDAO iQualityInspectionDAO;
	
	@Override
	public ResponseAsList getApprVendorDtls(VendorApprDtlReq vendorApprDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<VendorMstEntity> list = new ArrayList<VendorMstEntity>();

		try {
			if(vendorApprDtlReq.getVenRatingBased()!=null && vendorApprDtlReq.getVenRatingBased().equalsIgnoreCase("1")) {
				list = iVendorDAO.getVendorRatingDtls(vendorApprDtlReq.getApproved(),vendorApprDtlReq.getTenantId());
			}else {
				list = iVendorDAO.getApprVendorDtls(vendorApprDtlReq.getApproved(),vendorApprDtlReq.getTenantId());
			}
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getApprVendorDtls service error " + ex);
		}
		return returnList;
	}
	
	@Override
	public ResponseAsMessage insertVendorDtls(InsertVendorReq vendorInsertDtlReq) {
		
		ResponseAsMessage returnList = new ResponseAsMessage();
		
		String vendorName = vendorInsertDtlReq.getVendorName();
		String gst = vendorInsertDtlReq.getGst();
		String pan = vendorInsertDtlReq.getPan();
		String arn = vendorInsertDtlReq.getArn();
		String email = vendorInsertDtlReq.getEmailId();
		String vendorStatus = vendorInsertDtlReq.getVendorStatus();
		String poType = vendorInsertDtlReq.getPoType();
		String tenantId = vendorInsertDtlReq.getTenantId();
		String locReferName = vendorInsertDtlReq.getLocationReferenceName();
		String locAddLine = vendorInsertDtlReq.getLocationAddressLine();
		String city = vendorInsertDtlReq.getLocationCity();
        String state = vendorInsertDtlReq.getLocationState();
        String countryCode = vendorInsertDtlReq.getLocationConutryCode();
        String pinCode = vendorInsertDtlReq.getLocationPinCode();
        String vendorCode = vendorInsertDtlReq.getVendorCode();
        String locationId = vendorInsertDtlReq.getLocationId();
        String contactNo = vendorInsertDtlReq.getContactNo();
        String supCategory = vendorInsertDtlReq.getSupplyCategory();
        String gstType = vendorInsertDtlReq.getGstType();
		String currencyType = vendorInsertDtlReq.getCurrencyType();
        
        int locId = 0;
        int insertEle = 0;
        int updateVen = 0;
        int updateLoc = 0;
        String supplier = "";
        String vendorCat = "";
		try {

			
			if(vendorCode.isEmpty() && locationId.isEmpty()) {
				
				int checkVendorname = iVendorDAO.checkVendorname(vendorName, tenantId);
				if(checkVendorname ==0) {
				locId = iVendorDAO.insertLocDtls(tenantId, locReferName, locAddLine,
						 city, state, countryCode, pinCode);
				vendorCat = iVendorDAO.checkSupplyCatCodeExist(tenantId, supCategory);
				if (vendorCat.equalsIgnoreCase("NA")) {
					supplier = iVendorDAO.insertVendorCategory(supCategory, tenantId);
					vendorCat = supplier;
				}
				if(locId > 0) {
				insertEle = iVendorDAO.insertVendorDtls(vendorName,gst,pan,arn,email,
						vendorStatus,poType,tenantId,locId,contactNo,vendorInsertDtlReq.getVendorType(),vendorCat,gstType, currencyType);
				 if(insertEle == 1) {
					    returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successUpdated);
				       }else{
						returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
						returnList.setResponseMessage(ResponseMessageMap.failTouploadMsg);
					   }
				}
				
				}else {
					returnList.setResponseCode(ResponseMessageMap.responseAlreadyExists);
					returnList.setResponseMessage(ResponseMessageMap.vendorAlreadyExist);
				}
			}
			else{
				vendorCat = iVendorDAO.checkSupplyCatCodeExist(tenantId, supCategory);
				if (vendorCat.equalsIgnoreCase("NA")) {
					supplier = iVendorDAO.insertVendorCategory(supCategory, tenantId);
					vendorCat = supplier;
				}
				updateVen = iVendorDAO.updateVendorDtls(vendorCode,vendorName,gst,pan,arn,email,vendorStatus,
						poType,tenantId,locationId,contactNo,vendorInsertDtlReq.getVendorType(),vendorCat,gstType, currencyType);
				updateLoc = iVendorDAO.updateLocDtls(locReferName,locAddLine,city,state,countryCode,pinCode,
						locationId,tenantId);
				 if(updateVen == 1 && updateLoc == 1 ) {
					    returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
						returnList.setResponseMessage(ResponseMessageMap.successUpdated);
				       }else {
						returnList.setResponseCode(ResponseMessageMap.failToupdateCode);
						returnList.setResponseMessage(ResponseMessageMap.failToupdateMsg);
					   }
			}	
		 } catch (Exception ex) {
			logger.error("insertVendorDtls service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getAllVendorDtls(VendorAllDtlReq vendorApprDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<VendorMstEntity> list = new ArrayList<VendorMstEntity>();
		

		try {
			list = iVendorDAO.getAllVendorDtls(vendorApprDtlReq.getVendorCode(),vendorApprDtlReq.getTenantId());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getAllVendorDtls service error " + ex);
		}
		
		return returnList;
	}

	@Override
	public ResponseAsList getAllCustomerDtl(TenantRequest tenantRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<CustomerMstEntity> list = new ArrayList<CustomerMstEntity>();
		try {
			list = iVendorDAO.getAllCustomerDtl(tenantRequest.getTenantID());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception ex) {
			logger.error("getAllCustomerDtl Exception "+ex);
		}
		return returnList;
	}
	
	@Override
	public ResponseAsList getVendorCategory(TenantRequest tenantRequest) {
		ResponseAsList returnList = new ResponseAsList();
		List<VendorCategoryEntity> list = new ArrayList<>();
		try {
			list = iVendorDAO.getVendorCategory(tenantRequest.getTenantID());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception ex) {
			logger.error("getVendorCategory Exception "+ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getVendorInspRatingDtls(VendorInspRatingRequest req) {
		ResponseAsList returnList = new ResponseAsList();
		List<VendorRatingEntity> list = new ArrayList<VendorRatingEntity>();
		List<VendorRatingResponse> mainList = new ArrayList<VendorRatingResponse>();
		VendorRatingResponse sublist = new VendorRatingResponse();
		int newRating=0;
		try {
			list = iVendorDAO.getVendorInspRatingDtls(req.getVendorCode());
			for(int i=0;i<list.size();i++) {
				String dmId=iVendorDAO.getDmId(list.get(i).getVdtlId(), "DC078", "FC041");
				list.get(i).setDmId(dmId);
			}
			
			String designCode = uploadManagementDAO.getDesigCodeByEmpId(req.getEmpId(),
					req.getTenantId());
			
			int currSeq=iVendorDAO.getVendorCurrSeq(req.getVendorCode());
			int isApprove=indentUploadDAO.getApprovebtnEnableByCurr(designCode, req.getTenantId(), "DC078", String.valueOf(currSeq));
			if(isApprove==1) {
			int inspectionRaised=iVendorDAO.getInspectionRaisedVal(req.getVendorCode(), req.getTenantId());
			if(inspectionRaised==1 && currSeq==2) {
				sublist.setInspRaisedBtn("0");
				newRating=1;
			}else {
				int dateExceeded=iVendorDAO.checkInspectionDate(req.getVendorCode(), req.getTenantId());
				sublist.setInspRaisedBtn(String.valueOf(dateExceeded));
			}
			}else {
				sublist.setInspRaisedBtn("0");
				
			}
			sublist.setNewRating(String.valueOf(newRating));
			sublist.setNextInspectionOn(iVendorDAO.getNextInspDate(req.getVendorCode(), req.getTenantId()));
			sublist.setInspReqRaised(iVendorDAO.getInspRaisedBtn(req.getVendorCode(), req.getTenantId()));
			sublist.setList(list);
			mainList.add(sublist);
			
			if (mainList.size() > 0) {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(mainList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception ex) {
			logger.error("getVendorInspRatingDtls Exception "+ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage updateInspectionRaised(VendorInspRatingRequest req) {
		ResponseAsMessage res = new ResponseAsMessage();
		int updateStatus=0;
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {
			 updateStatus=iVendorDAO.updateInspectionRaised(req.getVendorCode());
				if (updateStatus == 1) {
					String vendorDtlId=iVendorDAO.getLatestVenDtlId(req.getVendorCode());
					if(vendorDtlId.equalsIgnoreCase("")) {
						vendorDtlId = iVendorDAO.getvDtlId();
					}
					String nextApprDesig = indentUploadDAO.getIndentNxtAppDesc("DC078", "1", "default",req.getTenantId());
					String uniqueCode= iIndentGroupDAO.getVendorUniqueCodeByVendorCode(req.getVendorCode());
					String vendorName = iIndentGroupDAO.getVendorNameByVendorCode(req.getVendorCode());
					String notify = String.format("%s-%s", uniqueCode, vendorName);
					messageList.add(notify);
					commonNotifyMethod.InvokeNotificationMethod(2, 53, req.getEmpId(), req.getTenantId(), messageList, otherEmpId, "0", null,null, nextApprDesig);
					commonNotifyMethod.InvokeApprovalDesigMethod(null, "DC078", vendorDtlId,null, req.getTenantId(), req.getEmpId(),nextApprDesig, null, notify);
	
					res.setResponseCode(ResponseMessageMap.responseCodeOk);
					res.setResponseDataMessage(ResponseMessageMap.successMsg);
					res.setResponseMessage(ResponseMessageMap.successUpdated);
				} else {
					res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					res.setResponseDataMessage(ResponseMessageMap.failMsg);
					res.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				}
			
		} catch (Exception ex) {
			logger.error("updateInspectionRaised error " + ex);
		}
		return res;
	}

	@Override
	public ResponseAsMessage vendorDtlInsert(JSONObject jsonObj, MultipartFile file) {
		ResponseAsMessage res = new ResponseAsMessage();
		String vendorCode="",inspectionDate="",inspectionRating="",empId="";
		String tenantId="",type="";
		List<String> messageList = new ArrayList<>();
		List<String> otherEmpId = new ArrayList<>();
		try {
			JSONArray iluoArray = jsonObj.getJSONArray("reqObj");
			for (int l = 0; l < iluoArray.length(); l++) {
				JSONObject iluoobjects = iluoArray.getJSONObject(l);
				JSONArray iluobodykeys = iluoobjects.names();
				for (int k = 0; k < iluobodykeys.length(); ++k) {
					String key = iluobodykeys.getString(k);
					String value = iluoobjects.getString(key);
					if (key.equalsIgnoreCase("vendorCode")) {
						vendorCode = value;
					} else if (key.equalsIgnoreCase("inspectionDate")) {
						inspectionDate = value;
					} else if (key.equalsIgnoreCase("inspectionRating")) {
						inspectionRating = value;
					}  else if (key.equalsIgnoreCase("inspectedBy")) {
						empId = value;
					} else if (key.equalsIgnoreCase("tenantId")) {
						tenantId = value;
					} else if (key.equalsIgnoreCase("type")) {
						type = value;
					}


				}
			}
		      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        LocalDate date = LocalDate.parse(inspectionDate, formatter);
		        LocalDate calInspecDate;
			if (inspectionRating.equalsIgnoreCase("A") || inspectionRating.equalsIgnoreCase("B")) {
				calInspecDate  = date.plusYears(1);
			} else {
				calInspecDate = date.plusMonths(1);
			}
			String vendorDtlId=iVendorDAO.getLatestVenDtlId(vendorCode);
			// update vendor dtl is_latest
			iVendorDAO.updateVendorIsLatest(vendorCode);
			int vdtlId = iVendorDAO.vendorDtlInsert(vendorCode,inspectionDate,inspectionRating,empId);
			if (vdtlId > 0) {
				// update inspection Date in vendor_mst
				iVendorDAO.updateInspectionDate(vendorCode, String.valueOf(calInspecDate));
				if(vendorDtlId.equalsIgnoreCase("")) {
					vendorDtlId = String.valueOf(vdtlId);
				}
	//			String nextApprDesig = poDAO.getAppDesig("DC078", "1",tenantId);
				
				String nextApprDesig = indentUploadDAO.getIndentNxtAppDesc("DC078", "2", "default", tenantId);
				String uniqueCode= iIndentGroupDAO.getVendorUniqueCodeByVendorCode(vendorCode);
				String vendorName = iIndentGroupDAO.getVendorNameByVendorCode(vendorCode);
				String notify = String.format("%s-%s", uniqueCode, vendorName);
				messageList.add(notify);
//				messageList.add(uniqueCode);
//				messageList.add(vendorCode);
				
				commonNotifyMethod.InvokeNotificationMethod(2, 54, empId, tenantId, messageList, otherEmpId, "0", null,null, nextApprDesig);
				commonNotifyMethod.InvokeApprovalDesigMethod(null, "DC078", vendorDtlId,null, tenantId, null,nextApprDesig, null,notify);
				
				if (file != null && !file.isEmpty()) {
					int version = 0;
					int checkCount = uploadManagementDAO.getCountByComb(tenantId, "FC041", String.valueOf(vdtlId),null);
					if (checkCount > 0) {
						version = uploadManagementDAO.getLatestVersionbycomb(tenantId, "FC041", String.valueOf(vdtlId),null);
						version = version + 1;
					} else {
						version = 1;
					}
					String getfileName = file.getOriginalFilename();
					String fileName = getfileName.substring(0, getfileName.lastIndexOf('.'));
					int newDmId = uploadManagementDAO.insertDocumentDtls(null, null, fileName, String.valueOf(vdtlId), null,
							"FC041", version, tenantId, "Vendor Document", "1", "0", "DC078");

					if (newDmId > 0) {
						int insertFileDtls = uploadManagementDAO.insertNewFileDtl(file, tenantId, newDmId, "FC041",
								empId, version, "DC078", type, String.valueOf(newDmId));
						if (insertFileDtls == 0) {
							res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
							res.setResponseDataMessage(ResponseMessageMap.failMsg);
							res.setResponseMessage(ResponseMessageMap.failTouploadMsg);
							return res;
						}
					}
				}
				res.setResponseCode(ResponseMessageMap.responseCodeOk);
				res.setResponseDataMessage(ResponseMessageMap.successMsg);
				res.setResponseMessage(ResponseMessageMap.successUpdated);
			} else {
				res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				res.setResponseDataMessage(ResponseMessageMap.failMsg);
				res.setResponseMessage(ResponseMessageMap.failToupdateMsg);
			}
			

		} catch (Exception ex) {
			logger.error("vendorDtlInsert error " + ex);
		}
		return res;
	}

	@Override
	public ResponseAsMessage customerComplaintCheck(CustomerComplaintCheck customerComplaintCheck) {
		ResponseAsMessage res = new ResponseAsMessage();
		int updateStatus=0;
		try {
			String qtyRating = "0";
			String inwardRating = "0";
			String relationShipRating = "0";
			BigDecimal internalCal =BigDecimal.ZERO;
			BigDecimal caExternaCal=BigDecimal.ZERO;
			BigDecimal reworkExternalCal=BigDecimal.ZERO;
			BigDecimal qualityRating=BigDecimal.ZERO;
			if(customerComplaintCheck.getCheckVal().equalsIgnoreCase("0")) {
				List<QualityInspectionHdrEntity>	qiHdrDtlReq=iQualityInspectionDAO.getQiHdrListtByQiHdrId(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId());
				if (qiHdrDtlReq.size() >0) {
					if(qiHdrDtlReq.get(0).getNrFlag().equalsIgnoreCase("0")) {
						internalCal = new BigDecimal(qiHdrDtlReq.get(0).getTotalOkQty())
				                .add(new BigDecimal(qiHdrDtlReq.get(0).getCaInternal()))
				                .add(new BigDecimal(qiHdrDtlReq.get(0).getReworkInternal()))
				                .add(new BigDecimal(qiHdrDtlReq.get(0).getRejectedInternal()))
				                .divide(new BigDecimal(qiHdrDtlReq.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
				                .multiply(new BigDecimal("100"));
							   
						caExternaCal = new BigDecimal(qiHdrDtlReq.get(0).getCaVendor())
				                	.divide(new BigDecimal(qiHdrDtlReq.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
				                    .multiply(new BigDecimal("75"));
		
						reworkExternalCal = new BigDecimal(qiHdrDtlReq.get(0).getReworkVendor())
				                        .divide(new BigDecimal(qiHdrDtlReq.get(0).getInspectionQty()), 2, RoundingMode.HALF_DOWN)
				                        .multiply(new BigDecimal("50"));
					 
						qualityRating=internalCal.add(caExternaCal).add(reworkExternalCal);
					}else {
						qualityRating = BigDecimal.ZERO;
					}
					qtyRating = qualityRating.toString();
			}
				String poDelDate=iQualityInspectionDAO.getPoDelDate(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId());
				String insDate=iQualityInspectionDAO.getInsDate(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId());
			   
				
				inwardRating =iQualityInspectionDAO.getInwardRating(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId(),poDelDate,insDate);
			 //   updateVendorRatingRequest.getSuppl	ierValue()
			    relationShipRating = iQualityInspectionDAO.getOldRelationshipRating(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId());
			}
			updateStatus = iVendorDAO.updateQtyInspectionRating(customerComplaintCheck.getHdrId(),customerComplaintCheck.getTenantId(), qtyRating, inwardRating,relationShipRating, customerComplaintCheck.getCheckVal());
				if (updateStatus == 1) {
//				
					res.setResponseCode(ResponseMessageMap.responseCodeOk);
					res.setResponseDataMessage(ResponseMessageMap.successMsg);
					res.setResponseMessage(ResponseMessageMap.successUpdated);
				} else {
					res.setResponseCode(ResponseMessageMap.responseCodeNotOk);
					res.setResponseDataMessage(ResponseMessageMap.failMsg);
					res.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				}
			
		} catch (Exception ex) {
			logger.error("customerComplaintCheck error " + ex);
		}
		return res;
	}

	@Override
	public ResponseAsMessage updateCustomerdtl(CustomerMstEntity updateCus) {
		// TODO Auto-generated method stub
		ResponseAsMessage response = new ResponseAsMessage();

        try {
        	int updateStatus=iVendorDAO.updateCusMasDtl(updateCus);
        	
        	if (updateStatus == 1) {
        		response.setResponseCode(ResponseMessageMap.responseCodeOk);
				response.setResponseDataMessage(ResponseMessageMap.success);
				response.setResponseMessage(ResponseMessageMap.successUpdated);
        	}else {
        		response.setResponseCode(ResponseMessageMap.failToupdateCode);
        		response.setResponseDataMessage(ResponseMessageMap.failMsg);
        		response.setResponseMessage(ResponseMessageMap.failToupdateMsg);
        	}
        } catch (Exception e) {
            logger.error("updateCustomerdtl error " + e);
        }

        return response;
    }
		
		


}
