package com.vmfg.mis.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.mis.dao.interfaces.iReportSchedulerDAO;
import com.vmfg.mis.dao.interfaces.iReportSchedulerVIIDAO;
import com.vmfg.mis.services.interfaces.iReportSchedulerVIIService;
import com.vmfg.project.entity.IndentHdrEntity;
import com.vmfg.project.entity.ProjectHdrEntity;
import com.vmfg.project.entity.ReInspectionVendorMasterEntity;
import com.vmfg.util.CommonNotifyMethod;

@Service
public class ReportSchedulerVIIService implements iReportSchedulerVIIService {
	private static final Logger logger = LoggerFactory.getLogger(ReportSchedulerVIIService.class);

	@Autowired
	iReportSchedulerVIIDAO ireportSchedulerDAO;
	
	@Autowired
	iReportSchedulerDAO iReportScheduler;
	
	@Autowired
	private CommonNotifyMethod commonNotifyMethod;
	
	@Override
	public ResponseAsMessage populateMaterialReports() {
		ResponseAsMessage returnList = new ResponseAsMessage();
		int res=0;
		try {
			String tenantAllId = iReportScheduler.getOrgTenant();
			String[] tenantArrId = tenantAllId.split(",");
			String tenantId;
			for(int q=0;q<tenantArrId.length;q++) {
				tenantId = tenantArrId[q];
			List<ProjectHdrEntity> hdrList = new ArrayList<ProjectHdrEntity>();
			hdrList = ireportSchedulerDAO.getProjects(tenantId);
			for (int i = 0; i < hdrList.size(); i++) {
				List<IndentHdrEntity> indentList = new ArrayList<IndentHdrEntity>();
				indentList = ireportSchedulerDAO.getIndentsByProjectId(hdrList.get(i).getPmHdrId());
				for (int j = 0; j < indentList.size(); j++) {
					int hdrId = ireportSchedulerDAO.reportProjectTrackerCountCheck(hdrList.get(i).getPmHdrId(),
							indentList.get(j).getSbcCode());
					if (hdrId == 0) {
						// insert report_project_tracker
						if(hdrList.get(i).getCompletedDateTime()==null) {
							hdrId = ireportSchedulerDAO.reportProjectTrackerInsert1(hdrList.get(i).getPmHdrId(),
									hdrList.get(i).getCreatedDate(), indentList.get(j).getSbcCode(),
									indentList.get(j).getBudgetValue(), indentList.get(j).getBudgetAllocated(),
									indentList.get(j).getTargetValue(),hdrList.get(i).getIsCompleted(), hdrList.get(i).getLastUpdatedDateTime(),
									hdrList.get(i).getTenantId());
							
						}else {
							hdrId = ireportSchedulerDAO.reportProjectTrackerInsert(hdrList.get(i).getPmHdrId(),
									hdrList.get(i).getCreatedDate(), indentList.get(j).getSbcCode(),
									indentList.get(j).getBudgetValue(), indentList.get(j).getBudgetAllocated(),
									indentList.get(j).getTargetValue(), hdrList.get(i).getIsCompleted(),
									hdrList.get(i).getCompletedDateTime(), hdrList.get(i).getLastUpdatedDateTime(),
									hdrList.get(i).getTenantId());
						}
						
					} else {
						// update report_project_tracker
						
						if(hdrList.get(i).getCompletedDateTime()==null) {
							ireportSchedulerDAO.reportProjectTrackerUpdate1(hdrList.get(i).getPmHdrId(),
									hdrList.get(i).getCreatedDate(), indentList.get(j).getSbcCode(),
									indentList.get(j).getBudgetValue(), indentList.get(j).getBudgetAllocated(),
									indentList.get(j).getTargetValue(), hdrList.get(i).getIsCompleted(),
								    hdrList.get(i).getLastUpdatedDateTime(),
									hdrList.get(i).getTenantId(), hdrId);
						}else {
							ireportSchedulerDAO.reportProjectTrackerUpdate(hdrList.get(i).getPmHdrId(),
									hdrList.get(i).getCreatedDate(), indentList.get(j).getSbcCode(),
									indentList.get(j).getBudgetValue(), indentList.get(j).getBudgetAllocated(),
									indentList.get(j).getTargetValue(), hdrList.get(i).getIsCompleted(),
									hdrList.get(i).getCompletedDateTime(), hdrList.get(i).getLastUpdatedDateTime(),
									hdrList.get(i).getTenantId(), hdrId);
						}
						

					}
					BigDecimal saleContribution = ireportSchedulerDAO
							.reportGetSaleContribution(hdrList.get(i).getEnquiryId());
					
					BigDecimal saleValue = ireportSchedulerDAO
							.reportGetSaleValue(hdrList.get(i).getEnquiryId());
					
					
					// update sale value and sale contribution
					ireportSchedulerDAO.reportUpdateSaleValue(hdrList.get(i).getPmHdrId(), saleContribution,saleValue);

					// get materialCost group by sbc id from inventoryMaterialTransfer table then
					// update this to report project tracker
					
					BigDecimal materialCost = BigDecimal.ZERO;
					materialCost = ireportSchedulerDAO.getMatrialCostCount(hdrList.get(i).getPmHdrId(),
							indentList.get(j).getSbcCode());
//					if(materialCostCoutnCheck>0) {
//					 materialCost = ireportSchedulerDAO.getMatrialCost(hdrList.get(i).getPmHdrId(),
//								indentList.get(j).getSbcCode());
//					}
					res= ireportSchedulerDAO.updateMatrialCost(hdrList.get(i).getPmHdrId(), indentList.get(j).getSbcCode(),
							materialCost);
					
					BigDecimal humanCost = BigDecimal.ZERO;
					humanCost = ireportSchedulerDAO.getHumanCost(hdrList.get(i).getPmHdrId());
					ireportSchedulerDAO.updateHumanCost(hdrList.get(i).getPmHdrId(), indentList.get(j).getSbcCode(),
							humanCost);
				}
			}
			
			
			if (res == 1) {
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.successInserted);
			} else {
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
			}
			
			logger.info("populateMaterialReports Method End ");
			
		} catch (Exception ex) {
			logger.error("populateMaterialReports Method Exception " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsMessage NotifyScmForVendor() {
		logger.info("NotifyScmForVendor Method Started");
		ResponseAsMessage returnList = new ResponseAsMessage();
		try {
			List<String> otherEmpId = new ArrayList<>();
			List<ReInspectionVendorMasterEntity> vendorList = new ArrayList<ReInspectionVendorMasterEntity>();
			vendorList = ireportSchedulerDAO.getVendorList();
			
			List<String> messageList = vendorList.stream().map(ReInspectionVendorMasterEntity :: getMessage).collect(Collectors.toList());
			
			if(vendorList.size()>0) {
				String nextApprovingDesig =  ireportSchedulerDAO.getNextApprovingSequence(vendorList.get(0).getTenantId(),"5");
				commonNotifyMethod.InvokeNotificationMethod(2,0, "", vendorList.get(0).getTenantId(), messageList, otherEmpId, "1", null, null, nextApprovingDesig);
			}
			
		}catch(Exception ex) {
			logger.error("populateMaterialReports Method Exception " + ex);
		}
		
		logger.info("NotifyScmForVendor Method End");
		return returnList;
	}
}
