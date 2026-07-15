package com.vmfg.quality.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.impl.IndentUploadDAO;
import com.vmfg.general.dao.impl.DepartmentAndEmployeeDAO;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.quality.dao.interfaces.IQualityDAO;
import com.vmfg.quality.entity.GetInspTypeEntity;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.quality.entity.GetQtyInspectionHdrEntity;
import com.vmfg.quality.entity.RetieveQCInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.GetConfigNameReq;
import com.vmfg.quality.request.GetInspTypeReq;
import com.vmfg.quality.request.GetQtyDtlRequest;
import com.vmfg.quality.request.GetQtyInspectionHdrRequest;
import com.vmfg.quality.request.GetqtyInspecDocDtlRequest;
import com.vmfg.quality.request.RetieveQCInspectionHdrReq;
import com.vmfg.quality.request.RetrieveQualitInspectionReq;
import com.vmfg.quality.response.GetQtyInspectionHdrResponse;
import com.vmfg.quality.services.interfaces.IQualityServices;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.sales.dao.interfaces.IEnquiryDAO;
import com.vmfg.sales.entity.ApprovedDocEntity;
import com.vmfg.scm.dao.impl.IndentGroupDAO;

@Service
public class QualityServices implements IQualityServices {
	private static final Logger logger = LoggerFactory.getLogger(QualityServices.class);

	@Autowired
	IQualityDAO iQualityDAO;

	@Autowired
	IndentGroupDAO indentGroupDAO;

	@Autowired
	UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	IEnquiryDAO iEnquiryDAO;
	
	
	@Autowired
	IndentUploadDAO indentUploadDAO;
	
	@Autowired
	DepartmentAndEmployeeDAO departmentAndEmployeeDAO;
	
	@Override
	public ResponseAsList getQtyDtl(GetQtyDtlRequest getQtyDtlReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetQtyDtlEntity> list = new ArrayList<GetQtyDtlEntity>();

		try {
			list = iQualityDAO.getQtyDtl(getQtyDtlReq.getQHdrId(), getQtyDtlReq.getEmpId(), getQtyDtlReq.getFromDate(),
					getQtyDtlReq.getToDate(), getQtyDtlReq.getTenantId(), getQtyDtlReq.getCustomerName(),
					getQtyDtlReq.getPmId(),getQtyDtlReq.getProjectId());

			Map<String, GetQtyDtlEntity> uniqueProjects = new LinkedHashMap<>();
			for (GetQtyDtlEntity item : list) {
				if (!uniqueProjects.containsKey(item.getPmHdrId())) {
					uniqueProjects.put(item.getPmHdrId(), item);
				}
			}
			list = new ArrayList<>(uniqueProjects.values());

			for (int i = 0; i < list.size(); i++) {
				list.get(i).setQtyinspectionCompleted(iQualityDAO.getQtyinspCompleted(list.get(i).getPmHdrId(),getQtyDtlReq.getTenantId()));
				list.get(i).setQtyinspectionTotal(iQualityDAO.getCountofinsp(list.get(i).getPmHdrId(),getQtyDtlReq.getTenantId()));
				List<RetieveQCInspectionHdrEntity> inspectQuality = iQualityDAO
						.getQiCountsByPmHdrId(list.get(i).getPmHdrId(), getQtyDtlReq.getTenantId());
				list.get(i).setOkCount(inspectQuality.get(0).getOkCount());
				list.get(i).setRejectedCount(inspectQuality.get(0).getRejectedCount());
				list.get(i).setConditionalApprovedCnt(inspectQuality.get(0).getConditionalCnt());
				list.get(i).setReworkCount(inspectQuality.get(0).getReworkCount());
				list.get(i).setQtyToBeInspected(inspectQuality.get(0).getQtyToBeInspected());
				list.get(i).setQtyTotalCompleted(inspectQuality.get(0).getQtyInspectionCompleted());
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
			logger.error("getQtyDtl service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList retrieveQualitInspectionReq(RetrieveQualitInspectionReq inspectionReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetrieveQualitInspectionEntity> list = new ArrayList<>();

		try {
			list = iQualityDAO.retrieveQualitInspectionReq(inspectionReq.getProjectId(), inspectionReq.getTenantId());
			String designCode = uploadManagementDAO.getDesigCodeByEmpId(inspectionReq.getEmpId(),
					inspectionReq.getTenantId());
			String getDef = iEnquiryDAO.setDefaultUser(inspectionReq.getPmId(),inspectionReq.getTenantId());
//			String empArr[] = getDef.split(",");
//			int isflag = 0;
//			for(int q =0;q<empArr.length;q++) {
//				if(empArr[q].equalsIgnoreCase(designCode)) {
//					isflag = isflag +1;
//				}
//			}
			Set<String> defaultUsers = new HashSet<>(Arrays.asList(getDef.split(",")));
			int isflag = defaultUsers.contains(designCode) ? 1 : 0;
			
			if (list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
////					if(list.get(i).getQcRequestedFrom().equalsIgnoreCase("MI") && list.get(i).getNrFlag() == null) {
//					if(list.get(i).getNrFlag() == null) {
//						list.get(i).setIsnrFlag(isflag);
//					}else {
//						list.get(i).setIsnrFlag(0);
//					}
//					String insStatus = iQualityDAO.checkInsCount(list.get(i).getQiId());
//					if (insStatus.equalsIgnoreCase("NA")) {
//						list.get(i).setInspectionStatus("Yet To Inspect");
//						list.get(i).setInspectFlag("1");
//					} else {
//						list.get(i).setInspectionStatus(insStatus);
//						list.get(i).setInspectFlag("0");
//					}
//				}
				list.forEach(item -> {
			        if (item.getNrFlag() == null) {
			            item.setIsnrFlag(isflag);
			        } else {
			            item.setIsnrFlag(0);
			        }
			    });
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("retrieveQualitInspectionReq service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList retieveQCInspectionHdr(RetieveQCInspectionHdrReq retieveQCInspection) {
		ResponseAsList returnList = new ResponseAsList();
		List<RetieveQCInspectionHdrEntity> list = new ArrayList<>();
		int dmId = 0;
		try {
			list = iQualityDAO.retieveQCInspectionHdr(retieveQCInspection.getQiId(), retieveQCInspection.getTenantId());
			for (int i = 0; i < list.size(); i++) {
				dmId = indentUploadDAO.getDmIdByLatestVerion(list.get(i).getIndentDtlId(),
						retieveQCInspection.getTenantId());
				list.get(i).setDmId(dmId);
				String desigCode = uploadManagementDAO.getDesigCodeByEmpId(retieveQCInspection.getEmpId(), retieveQCInspection.getTenantId());
				String approveSeq = departmentAndEmployeeDAO.getPrimaryDocFlagVal(desigCode,retieveQCInspection.getPmId(),retieveQCInspection.getTenantId());
				list.get(i).setMasterPoc(approveSeq);
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
			logger.error("retieveQCInspectionHdr service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getInspType(GetInspTypeReq inspecType) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetInspTypeEntity> list = new ArrayList<>();

		try {
			list = iQualityDAO.getInspType(inspecType.getTenantId());

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
			logger.error("getInspType service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getConfigName(GetConfigNameReq inspecType) {
		ResponseAsList returnList = new ResponseAsList();
		List<String> list = new ArrayList<>();

		try {
			String configName = iQualityDAO.getConfigNameByQiId(inspecType.getQiId(), inspecType.getTenantId());
			if (!configName.equalsIgnoreCase("NA")) {
				list.add(configName);
			} else {
				list = iQualityDAO.getConfigName(inspecType.getTenantId());
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
			logger.error("getInspType service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getQtyInspectionHdr(GetQtyInspectionHdrRequest getQtyInspectionReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<GetQtyInspectionHdrEntity> list = new ArrayList<GetQtyInspectionHdrEntity>();
		List<GetQtyInspectionHdrResponse> respList = new ArrayList<GetQtyInspectionHdrResponse>();
		try {
			String vendorName="";
			String vendorCode="";
			if(!getQtyInspectionReq.getVendor().equalsIgnoreCase("getAll")) {
				vendorName = indentGroupDAO.getVendorNameByVendorCode(getQtyInspectionReq.getVendor());
				vendorCode=getQtyInspectionReq.getVendor();
			}
			
			GetQtyInspectionHdrResponse resp = new GetQtyInspectionHdrResponse();
			list = iQualityDAO.getInspectionDtlList(getQtyInspectionReq.getFromDate(), getQtyInspectionReq.getToDate(),
					vendorCode, getQtyInspectionReq.getTenantId());
			int isEmpScmCheck=iQualityDAO.checkScmEmp(getQtyInspectionReq.getEmpId(),getQtyInspectionReq.getTenantId(),"D05");
			int isEmpQtyCheck=iQualityDAO.checkScmEmp(getQtyInspectionReq.getEmpId(),getQtyInspectionReq.getTenantId(),"D09");
			for(int i=0;i<list.size();i++) {
				if(isEmpScmCheck>0) {
					list.get(i).setInputEnable(1);
				}else {
					list.get(i).setInputEnable(0);
				}
				
				if(isEmpQtyCheck>0) {
					list.get(i).setInputQtyEnable(1);
				}else {
					list.get(i).setInputQtyEnable(0);
				}
				list.get(i).setOkQty(new BigDecimal (list.get(i).getOkQty()).subtract(new BigDecimal (list.get(i).getCaVendor())).subtract(new BigDecimal (list.get(i).getCaInternal())).toString());
			}
			if (list.size() > 0) {
				
				resp.setQtyinspectionList(list);
				resp.setTotalCaInternal(list.stream().map(x -> new BigDecimal(x.getCaInternal()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalCaVendor(list.stream().map(x -> new BigDecimal(x.getCaVendor()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalReworkInternal(list.stream().map(x -> new BigDecimal(x.getReworkInternal()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalReworkVendor(list.stream().map(x -> new BigDecimal(x.getReworkVendor()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalRejectedInternal(list.stream().map(x -> new BigDecimal(x.getRejectedInternal()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalRejectedExternal(list.stream().map(x -> new BigDecimal(x.getRejectedExternal()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalOkty(list.stream().map(x -> new BigDecimal(x.getOkQty()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setTotalInspectionQty(list.stream().map(x -> new BigDecimal(x.getInspectionQty()))
						.reduce(BigDecimal.ZERO, BigDecimal::add).toString());
				resp.setVendorCode(getQtyInspectionReq.getVendor());
				resp.setVendorName(vendorName);
				respList.add(resp);
			}
			if (respList.size() > 0) {
				returnList.setResponseData(respList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(respList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("getQtyInspectionHdr service error " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getqtyInspecDocDtl(GetqtyInspecDocDtlRequest getqtyInspecDocDtlReq) {
		List<ApprovedDocEntity> list = new ArrayList<ApprovedDocEntity>();
		ResponseAsList returnList = new ResponseAsList();

		try {
			list =iQualityDAO.getqtyInspecDocDtl(getqtyInspecDocDtlReq);
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
			logger.error("getInspType service error " + ex);
		}
		return returnList;
	}

}
