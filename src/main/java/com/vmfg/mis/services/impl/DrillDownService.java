package com.vmfg.mis.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.inventory.entity.InvProdDrillEntity;
import com.vmfg.inventory.entity.NoOfPoDrillEntity;
import com.vmfg.mis.dao.interfaces.IDrillDownDAO;
import com.vmfg.mis.entity.DelayedEntity;
import com.vmfg.mis.entity.DrilldownEntity;
import com.vmfg.mis.entity.InitialScopValuePOEntity;
import com.vmfg.mis.request.DelayedIndentRequest;
import com.vmfg.mis.request.DrillDownRequest;
import com.vmfg.mis.services.interfaces.IDrillDownService;
import com.vmfg.project.dao.impl.ProjectDAO;
import java.util.stream.Collectors;
@Service
public class DrillDownService implements IDrillDownService {
	private static final Logger logger = LoggerFactory.getLogger(DrillDownService.class);

	@Autowired
	IDrillDownDAO iDrillDownDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	//new code 
	@Override
	public ResponseAsList getIndentByNotAvailablePO(DrillDownRequest drillDownRequest) {
	    ResponseAsList returnList = new ResponseAsList();
	    String tenantId = drillDownRequest.getTenantId();
	    String projectId = drillDownRequest.getProjectId();
	    String pmId = drillDownRequest.getPmId();
	    String empId = drillDownRequest.getEmpId();
	    
	    if (projectId.equalsIgnoreCase("getall")) {
	        projectId = "%%";
	    } else {
	        projectId = "%" + projectId + "%";
	    }
	    
	    String month = drillDownRequest.getMonthYear().split("-")[0];
	    String year = drillDownRequest.getMonthYear().split("-")[1];
	    String monthYear = "";
	    if (drillDownRequest.getLifeSpan().equalsIgnoreCase("0")) {
	        monthYear = "and month(inhdr.CREATED_DATE)='" + month + "' and year(inhdr.CREATED_DATE)='" + year + "'";
	    }
	    
	    List<DrilldownEntity> list = new ArrayList<>();
	    
	    try {
	        list = iDrillDownDAO.getIndentByNotAvailablePOList(tenantId, projectId, pmId, empId, monthYear);
	        String masterPoc = iDrillDownDAO.getPrimaryDocFlagVal(empId, pmId, tenantId);
	        List<String> indentDtlIds = list.stream()
	                .map(DrilldownEntity::getIndentDtlId)
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());

	        Map<String, String> pjsCheckMap = iDrillDownDAO.getPjsCheck(indentDtlIds, tenantId);
	        Map<String, String> poCheckMap = iDrillDownDAO.getPoCheck(indentDtlIds, tenantId);
	        Map<String, String> indentCheckMap = iDrillDownDAO.getIndentCheck(indentDtlIds, tenantId);
	        Map<String, String> assignedBy = iDrillDownDAO.getAssignedBy(indentDtlIds, tenantId);
	        
	        for (DrilldownEntity entity : list) {
	            if (entity.getIndentDtlId() != null) {
	            	String emp = assignedBy.getOrDefault(entity.getIndentDtlId(), "");
	            	if(!emp.isEmpty()) {
	            		 entity.setAssignedPerson(emp);
	            	}
	                String pjsCheck = pjsCheckMap.getOrDefault(entity.getIndentDtlId(), "");

	                if(!pjsCheck.equalsIgnoreCase("") && !pjsCheck.equalsIgnoreCase(null) && 
							pjsCheck.equalsIgnoreCase("1")) {
	                    String poCheck = poCheckMap.getOrDefault(entity.getIndentDtlId(), "");
	                    if ("0".equalsIgnoreCase(poCheck) || poCheck.isEmpty()) {
	                        entity.setStage("PO");
	                    }
	                } else if ("0".equalsIgnoreCase(pjsCheck)) {
	                    entity.setStage("PJS");
	                } else {
	                    String indentCheck = indentCheckMap.getOrDefault(entity.getIndentDtlId(), "");
	                    if(indentCheck.equalsIgnoreCase("0") || indentCheck.equalsIgnoreCase("") 
								|| indentCheck.equalsIgnoreCase(null)) {
	                        entity.setStage("Indent");
	                    }
	                }
	            }
	        }
	        
	        List<DrilldownEntity> filteredList =
	                "".equals(masterPoc)
	                    ? list  // or Collections.emptyList()
	                    : list.stream()
	                          .filter(e -> masterPoc.equals(e.getAssignedPerson()))
	                          .collect(Collectors.toList());

	        if (!filteredList.isEmpty()) {
	            returnList.setResponseData(filteredList);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
	            returnList.setResponseMessage(ResponseMessageMap.success);
	        } else {
	            returnList.setResponseData(filteredList);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
	            returnList.setResponseMessage(ResponseMessageMap.noRecord);
	        }
	    } catch (Exception e) {
	        logger.error("getIndentByNotAvailablePO service error " + e);
	    }
	    
	    return returnList;
	}
	@Override
	public ResponseAsList getIndentByInventoryStock(DrillDownRequest drillDownRequest) {
	    ResponseAsList returnList = new ResponseAsList();
	    String tenantId = drillDownRequest.getTenantId();
	    String projectId = drillDownRequest.getProjectId();
	    String pmId = drillDownRequest.getPmId();
	    String empId = drillDownRequest.getEmpId();
	    
	    if (projectId.equalsIgnoreCase("getall")) {
	        projectId = "%%";
	    } else {
	        projectId = "%" + projectId + "%";
	    }
	    
	    String month = drillDownRequest.getMonthYear().split("-")[0];
	    String year = drillDownRequest.getMonthYear().split("-")[1];
	    String monthYear = "";
	    if (drillDownRequest.getLifeSpan().equalsIgnoreCase("0")) {
	        monthYear = "and month(inhdr.CREATED_DATE)='" + month + "' and year(inhdr.CREATED_DATE)='" + year + "'";
	    }
	    
	    List<DrilldownEntity> list = new ArrayList<>();
	    
	    try {
	        list = iDrillDownDAO.getIndentByInventoryStock(tenantId, projectId, pmId, empId, monthYear);
	        
	        String masterPoc = iDrillDownDAO.getPrimaryDocFlagVal(empId, pmId, tenantId); 
	        List<String> indentDtlIds = list.stream()
	                .map(DrilldownEntity::getIndentDtlId)
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());

	        Map<String, String> pjsCheckMap = iDrillDownDAO.getPjsCheck(indentDtlIds, tenantId);
	        Map<String, String> poCheckMap = iDrillDownDAO.getPoCheck(indentDtlIds, tenantId);
	        Map<String, String> indentCheckMap = iDrillDownDAO.getIndentCheck(indentDtlIds, tenantId);
	        Map<String, String> assignedBy = iDrillDownDAO.getAssignedBy(indentDtlIds, tenantId);
	        
	        for (DrilldownEntity entity : list) {
	            if (entity.getIndentDtlId() != null) {
	            	String emp = assignedBy.getOrDefault(entity.getIndentDtlId(), "");
	            	if(!emp.isEmpty()) {
	            		 entity.setAssignedPerson(emp);
	            	}
	                String pjsCheck = pjsCheckMap.getOrDefault(entity.getIndentDtlId(), "");

	                if(!pjsCheck.equalsIgnoreCase("") && !pjsCheck.equalsIgnoreCase(null) && 
							pjsCheck.equalsIgnoreCase("1")) {
	                    String poCheck = poCheckMap.getOrDefault(entity.getIndentDtlId(), "");
	                    if ("0".equalsIgnoreCase(poCheck) || poCheck.isEmpty()) {
	                        entity.setStage("PO");
	                    }
	                } else if ("0".equalsIgnoreCase(pjsCheck)) {
	                    entity.setStage("PJS");
	                } else {
	                    String indentCheck = indentCheckMap.getOrDefault(entity.getIndentDtlId(), "");
	                    if(indentCheck.equalsIgnoreCase("0") || indentCheck.equalsIgnoreCase("") 
								|| indentCheck.equalsIgnoreCase(null)) {
	                        entity.setStage("Indent");
	                    }
	                }
	            }
	        }
	        
	        List<DrilldownEntity> filteredList =
	                "".equals(masterPoc)
	                    ? list  // or Collections.emptyList()
	                    : list.stream()
	                          .filter(e -> masterPoc.equals(e.getAssignedPerson()))
	                          .collect(Collectors.toList());

	        if (!filteredList.isEmpty()) {
	            returnList.setResponseData(filteredList);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
	            returnList.setResponseMessage(ResponseMessageMap.success);
	        } else {
	            returnList.setResponseData(filteredList);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
	            returnList.setResponseMessage(ResponseMessageMap.noRecord);
	        }
	    } catch (Exception e) {
	        logger.error("getIndentByInventoryStock service error " + e);
	    }
	    
	    return returnList;
	}
//old code 
	
	/*
	@Override
	public ResponseAsList getIndentByNotAvailablePO(DrillDownRequest drillDownRequest) {
		ResponseAsList returnList= new ResponseAsList();
		String tenantId = drillDownRequest.getTenantId();
		String projectId = drillDownRequest.getProjectId();
		String pmId = drillDownRequest.getPmId();
		String empId = drillDownRequest.getEmpId();
		if(projectId.equalsIgnoreCase("getall")) {
			projectId = "%%";
		}else {
			projectId = "%"+projectId+"%";
		}
		
		
		String month=drillDownRequest.getMonthYear().split("-")[0];
		String year=drillDownRequest.getMonthYear().split("-")[1];
		String monthYear="";
		if(drillDownRequest.getLifeSpan().equalsIgnoreCase("0")) {
			monthYear="and month(inhdr.CREATED_DATE)='"+month+"' and year(inhdr.CREATED_DATE)='"+year+"'";
		}
		
		
		List<DrilldownEntity> list = new ArrayList<DrilldownEntity>();
		try {	
			
				list = iDrillDownDAO.getIndentByNotAvailablePOList(tenantId,projectId,pmId,empId,monthYear);
				for(int i=0 ; i< list.size(); i++) {
					if(list.get(i).getIndentDtlId() != null) {
						String pjsCheck = iDrillDownDAO.getPjsCheck(list.get(i).getIndentDtlId(),tenantId);
						if(!pjsCheck.equalsIgnoreCase("") && !pjsCheck.equalsIgnoreCase(null) && 
								pjsCheck.equalsIgnoreCase("1")) {
							String poCheck = iDrillDownDAO.getPoCheck(list.get(i).getIndentDtlId(),tenantId);
							if(poCheck.equalsIgnoreCase("0") || poCheck.equalsIgnoreCase("") 
									|| poCheck.equalsIgnoreCase(null)) {
								list.get(i).setStage("PO");
							}
						}else if(pjsCheck.equalsIgnoreCase("0")){
							list.get(i).setStage("PJS");
						}else {
							String indentCheck = iDrillDownDAO.getIndentCheck(list.get(i).getIndentDtlId(),tenantId);
							if(indentCheck.equalsIgnoreCase("0") || indentCheck.equalsIgnoreCase("") 
									|| indentCheck.equalsIgnoreCase(null)) {
								list.get(i).setStage("Indent");
							}
						}
					}
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
		}catch(Exception e) {
			logger.error("getDesignWidgetDtl service error " + e);
		}
		return returnList;
	}
	
	*/

	
	
	
	@Override
	public ResponseAsList getDelayedIndent(DrillDownRequest delayedIndentRequest) {
		ResponseAsList returnList= new ResponseAsList();
		String tenantId = delayedIndentRequest.getTenantId();
		String pmId = delayedIndentRequest.getPmId();
		
		String projectId = delayedIndentRequest.getProjectId();
		String empId = delayedIndentRequest.getEmpId();
		
		if(projectId.equalsIgnoreCase("getall")) {
			projectId = "%%";
		}else {
			projectId = "%"+projectId+"%";
		}
		
		
		String month=delayedIndentRequest.getMonthYear().split("-")[0];
		String year=delayedIndentRequest.getMonthYear().split("-")[1];
		String monthYear="";
		if(delayedIndentRequest.getLifeSpan().equalsIgnoreCase("0")) {
			monthYear="and month(inhdr.CREATED_DATE)='"+month+"' and year(inhdr.CREATED_DATE)='"+year+"'";
		}
		
		List<DelayedEntity> list = new ArrayList<DelayedEntity>();
		try {	
				list = iDrillDownDAO.getDelayedIndentList(tenantId,projectId,pmId,empId,monthYear);
				
				List<String> indentDtlIds = list.stream()
		                .map(DelayedEntity::getIndentDtlId)
		                .filter(Objects::nonNull)
		                .collect(Collectors.toList());
				 Map<String, String> assignedBy = iDrillDownDAO.getAssignedBy(indentDtlIds, tenantId);
			        
			        for (DelayedEntity entity : list) {
			            if (entity.getIndentDtlId() != null) {
			            	String emp = assignedBy.getOrDefault(entity.getIndentDtlId(), "");
			            	if(!emp.isEmpty()) {
			            		 entity.setEmployeeName(emp);
			            	}
			            }
			        }
			    String masterPoc = iDrillDownDAO.getPrimaryDocFlagVal(empId, pmId, tenantId); 
			    
			    List<DelayedEntity> filteredList =
		                "".equals(masterPoc)
		                    ? list  // or Collections.emptyList()
		                    : list.stream()
		                          .filter(e -> masterPoc.equals(e.getEmployeeName()))
		                          .collect(Collectors.toList());
			        
			if (filteredList.size() > 0) {
				returnList.setResponseData(filteredList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(filteredList);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		}catch(Exception e) {
			logger.error("getDesignWidgetDtl service error " + e);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getPoInitalValue(DelayedIndentRequest getPoInitalValueRequest) {
		ResponseAsList returnList = new ResponseAsList();

		List<InitialScopValuePOEntity> list = new ArrayList<InitialScopValuePOEntity>();
		String projectId = getPoInitalValueRequest.getProjectId();
		if(projectId.equalsIgnoreCase("getall")) {
			projectId="%%";
		}else {
			projectId="%"+projectId+"%";
		}
		String month=getPoInitalValueRequest.getMonthYear().split("-")[0];
		String year=getPoInitalValueRequest.getMonthYear().split("-")[1];
		String monthYear="";
		if(getPoInitalValueRequest.getLifeSpan().equalsIgnoreCase("0")) {
			monthYear="and month(hdr.DATE)='"+month+"' and year(hdr.DATE)='"+year+"'";
		}
		
		try {
			list = iDrillDownDAO.getPoInitalValueList(projectId,
					getPoInitalValueRequest.getTenantId(),monthYear);
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
			logger.error("getPoInitalValue Method Exception " + ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getInventoryValueDrill(DelayedIndentRequest getgetInventoryValueRequest) {
		ResponseAsList returnList = new ResponseAsList();
		
		List<InvProdDrillEntity> list = new ArrayList<InvProdDrillEntity>();
		String pmId = getgetInventoryValueRequest.getPmId();
		if(pmId.equalsIgnoreCase("getall")) {
			pmId="%%";
		}else {
			pmId="%"+pmId+"%";
		}
		
		String projectId=getgetInventoryValueRequest.getProjectId();
		if(projectId.equalsIgnoreCase("getall")) {
			projectId="%%";
		}else {
			projectId="%"+projectId+"%";
		}
		
		try {
			list = iDrillDownDAO.getInventoryValueDrillList(pmId,getgetInventoryValueRequest.getTenantId(),projectId);
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
			logger.error("getInventoryValueDrill Method Exception "+ex);
		}
		return returnList;
	}

	@Override
	public ResponseAsList getNumberOfPoDrill(DelayedIndentRequest getgetInventoryValueRequest) {
		ResponseAsList returnList = new ResponseAsList();
		try {
			List<NoOfPoDrillEntity> list = new ArrayList<NoOfPoDrillEntity>();
			
			String projId = getgetInventoryValueRequest.getProjectId();
			if(projId.equalsIgnoreCase("getall")) {
				projId="%%";
			}else {
			//	pmId="%"+pmId+"%";
				projId=""+projId+"";
			}
			
			String month=getgetInventoryValueRequest.getMonthYear().split("-")[0];
			String year=getgetInventoryValueRequest.getMonthYear().split("-")[1];
			String monthYear="";	
			if(getgetInventoryValueRequest.getLifeSpan().equalsIgnoreCase("0")) {
				monthYear="and month(hdr.CREATED_DATE)='"+month+"' and year(hdr.CREATED_DATE)='"+year+"'";
			}
			
			
			list = iDrillDownDAO.getNumberOfPoDrillList(projId,getgetInventoryValueRequest.getTenantId(),getgetInventoryValueRequest.getEmpId(),
					getgetInventoryValueRequest.getPmId(),monthYear);
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
			logger.error("getNumberOfPoDrill Method Exception "+ex);
		}
		return returnList;
	}

	
}
