package com.vmfg.design.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vmfg.design.dao.interfaces.IDesignDAO;
import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.design.entity.GetKeySubAreaDtlEntity;
import com.vmfg.design.entity.GetTasKTemplateHdrEntity;
import com.vmfg.design.entity.ProductBasedInventoryDtlEntity;
import com.vmfg.design.entity.ProductBasedPoDtlEntity;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.entity.ProductMstEntity;
import com.vmfg.design.entity.ProjectKeyAreaMstEntity;
import com.vmfg.design.entity.getPoDetailByIndentDtlEntity;
import com.vmfg.design.request.DeletedesignSubKeyAreaRequest;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.design.request.GetKeySubAreaByPKIdRequest;
import com.vmfg.design.request.GetTasKTemplateHdrRequest;
import com.vmfg.design.request.ProductBasedInventoryDtlRequest;
//import com.vmfg.design.request.ProductBasedPoDtlRequest;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.request.UpdatedesignSubKeyAreaRequest;
import com.vmfg.design.request.getPoDetailByIndentDtlRequest;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.design.services.interfaces.IDesignService;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.interfaces.IProjectDAO;
import com.vmfg.sales.dao.impl.UploadManagementDAO;

@Service
public class DesignService implements IDesignService {
	private static final Logger logger = LoggerFactory.getLogger(DesignService.class);

	@Autowired
	IDesignDAO iDesignDAO;

	@Autowired
	UploadManagementDAO deptM;

	@Autowired
	IProjectDAO iProjectDAO;
	
	@Autowired
	private IIndentUploadDAO iIndentUploadDAO;

	@Override
	public ResponseAsList getDesignHdr(DesignRequest designReq) {
		ResponseAsList returnL = new ResponseAsList();
		try {
			List<DesignHdr> returnList = null;

			returnList = iDesignDAO.getDesignHdr(designReq.getFromDate(), designReq.getToDate(),
					designReq.getCustomer(), designReq.getProcessId(), designReq.getEmpId(), designReq.getTenantID(),
					designReq.getDesignID(),designReq.getProjectId());

			returnList.forEach(designHdr -> {

				String dept = deptM.getDepCodeByEmpId(designReq.getEmpId(), designReq.getTenantID());

				designHdr.setTaskPlan(
						iDesignDAO.getTaskPlanned(designHdr.getDesignID(), dept, designReq.getTenantID(), "%%"));

				designHdr.setTaskActual(
						iDesignDAO.getTaskPlanned(designHdr.getDesignID(), dept, designReq.getTenantID(), "%1%"));

				designHdr.setIndentPlan(
						iDesignDAO.getIndentPlanned(designHdr.getDesignID(), dept, designReq.getTenantID(), "%%"));
				designHdr.setIndentActual(
						iDesignDAO.getIndentPlanned(designHdr.getDesignID(), dept, designReq.getTenantID(), "%1%"));

			});

			if (returnList.size() > 0) {
				returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnL.setResponseMessage(ResponseMessageMap.success);
				returnL.setResponseData(returnList);
			} else {
				returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnL.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception ex) {
			logger.error("error in getDesignHdr service " + ex.getMessage());
		}

		return returnL;

	}

	@Override
	public ResponseAsList getKeyArea(ProductDtlDropDownRequest tentReq) {
		ResponseAsList returnL = new ResponseAsList();
		List<ProjectKeyAreaMstEntity> ka = iDesignDAO.getKeyArea(tentReq);
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}

	@Override
	public ResponseAsList getKeySubArea(ProductDtlDropDownRequest tentReq) {
		ResponseAsList returnL = new ResponseAsList();
		List<ProjectKeyAreaMstEntity> ka = iDesignDAO.getKeySubArea(tentReq);
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}

	@Override
	public ResponseAsList getAllProductsByPmHdrId(ProductDtlDropDownRequest productDtlDropDownReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<ProductMstDropDownEntity> ka = iDesignDAO.getAllProductsByPmHdrId(productDtlDropDownReq.getPmHdrId(),
				productDtlDropDownReq.getTenantId(),productDtlDropDownReq.getIsQuantity());
		if (ka.size() > 0) {
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
			returnList.setResponseData(ka);
		} else {
			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnList.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnList;
	}
	
	@Override
	public ResponseAsList getAllProductsDtl(ProductDtlDropDownRequest productDtlDropDownReq) {
		ResponseAsList returnList = new ResponseAsList();
		
		String pmHdrId = productDtlDropDownReq.getPmHdrId().equalsIgnoreCase("getall") ? "%%" : productDtlDropDownReq.getPmHdrId();
		List<ProductMstEntity> ka = iDesignDAO.getAllProductsByPmHdrId(pmHdrId,
				productDtlDropDownReq.getTenantId());
		
		List<ProductMstEntity> uniqueIndentDtlIds = ka.stream().filter(indent -> "1".equals(indent.getIsPdf()) || "0".equals(indent.getIsPdf()))
				.collect(Collectors.toList());
	
		uniqueIndentDtlIds.forEach(indentDtlId -> {
			int dmId = iIndentUploadDAO.getDmIdByLatestVerion(indentDtlId.getIndentDtlId(), productDtlDropDownReq.getTenantId());
			indentDtlId.setDmId(dmId);
		});
			
		if (ka.size() > 0) {
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
			returnList.setResponseData(ka);
		} else {
			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnList.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnList;
	}
	
//	@Override
//	public ResponseAsList getPoDetailByIndentDtl() {
//		ResponseAsList returnList = new ResponseAsList();
//		List<getPoDetailByIndentDtlEntity > ka = iDesignDAO.getAllProductsByIndentDtlId(IndentDtlId.getIndentDtlId(),
//				IndentDtlId.getTenantId());
//		if (ka.size() > 0) {
//			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
//			returnList.setResponseMessage(ResponseMessageMap.success);
//			returnList.setResponseData(ka);
//		} else {
//			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
//			returnList.setResponseMessage(ResponseMessageMap.noRecord);
//		}
//
//		return returnList;
//	}
	
	@Override
	public ResponseAsList getPoDetailByIndentDtl(getPoDetailByIndentDtlRequest indentDtlIdReq) {
		ResponseAsList returnList = new ResponseAsList();
		List<getPoDetailByIndentDtlEntity > ka = iDesignDAO.getPoDetailByIndentDtlRequest(indentDtlIdReq.getIndentDtlId(),
				indentDtlIdReq.getTenantId());
		if (ka.size() > 0) {
			returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnList.setResponseMessage(ResponseMessageMap.success);
			returnList.setResponseData(ka);
		} else {
			returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnList.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnList;
	}

	@Override
	public ResponseAsMessage updatedesignSubKeyArea(List<UpdatedesignSubKeyAreaRequest> updatedesignSubKeyAreaReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		int updateStatus = 0;
		String dskId = "";
		try {

			for (int i = 0; i < updatedesignSubKeyAreaReq.size(); i++) {
				int pskId = iProjectDAO.getCountProjectKeySubMst(updatedesignSubKeyAreaReq.get(i).getPskId(),
						updatedesignSubKeyAreaReq.get(i).getTenantId(),updatedesignSubKeyAreaReq.get(i).getPhHdrId());
				if (pskId == 0) {
					String lastCode = iProjectDAO.getLastKeySubCode(updatedesignSubKeyAreaReq.get(i).getTenantId(),updatedesignSubKeyAreaReq.get(i).getPhHdrId());
					String newCode = "";
					if(lastCode.equalsIgnoreCase("0")) {
						 newCode ="A";
					}else {
						 newCode = nextAlphabet(lastCode);
					}
					
					pskId = iProjectDAO.insertProjectKeySubArea(newCode, updatedesignSubKeyAreaReq.get(i).getPskId(),
							"1", updatedesignSubKeyAreaReq.get(i).getTenantId());
				}
				if (pskId > 0) {
					String subPskId = Integer.toString(pskId);
					dskId = updatedesignSubKeyAreaReq.get(i).getPksaId();
					if (dskId.equalsIgnoreCase("")) {
						int pskIdCheck = iProjectDAO.projectKeySubAreaCount(
								updatedesignSubKeyAreaReq.get(i).getPhHdrId(), subPskId,
								updatedesignSubKeyAreaReq.get(i).getTenantId(),updatedesignSubKeyAreaReq.get(i).getPkaId());
						if (pskIdCheck == 0) {
							updateStatus = iDesignDAO.insertDesignSubKey(updatedesignSubKeyAreaReq.get(i).getPhHdrId(),
									subPskId, updatedesignSubKeyAreaReq.get(i).getTenantId(),
									updatedesignSubKeyAreaReq.get(i).getPkaId());
							if (updateStatus > 0) {
								returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
								returnMessage.setResponseDataMessage("Success");
								returnMessage.setResponseMessage(ResponseMessageMap.successInserted);
							} else {

								returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
								returnMessage.setResponseDataMessage("Failure");
								returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
							}
						} else {
							returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMessage.setResponseDataMessage("Failure");
							returnMessage.setResponseMessage(ResponseMessageMap.responseAlreadyExistMsg);
						}
					} else {
						updateStatus = iDesignDAO.updateDesignSubKey(updatedesignSubKeyAreaReq.get(i).getPksaId(),
								updatedesignSubKeyAreaReq.get(i).getPhHdrId(), subPskId,
								updatedesignSubKeyAreaReq.get(i).getPkaId());
						if (updateStatus > 0) {
							returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
							returnMessage.setResponseDataMessage("Success");
							returnMessage.setResponseMessage(ResponseMessageMap.successInserted);
						} else {

							returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
							returnMessage.setResponseDataMessage("Failure");
							returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
						}
					}
				} else {
					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ResponseMessageMap.failToupdateMsg);
				}
			}

		} catch (Exception ex) {
			logger.error("updatedesignSubKeyArea error " + ex);
		}
		return returnMessage;
	}

	public String nextAlphabet(String currentAlphabet) {

		String nextAlphabet = getNextAlphabet(currentAlphabet);

		return nextAlphabet;
	}

	public static String getNextAlphabet(String currentAlphabet) {
		if (currentAlphabet.length() == 1) {
			char currentChar = currentAlphabet.charAt(0);
			if (currentChar == 'Z') {
				return "AA";
			} else {
				return String.valueOf((char) (currentChar + 1));
			}
		} else if (currentAlphabet.equalsIgnoreCase("AA")) {
			return "AB";
		} else if (currentAlphabet.equalsIgnoreCase("AAA")) {
			return "AAB";
		} else if (currentAlphabet.length() == 2) {
			if (currentAlphabet.equalsIgnoreCase("ZZ")) {
				return "AAA";
			} else {
				char lastChar = currentAlphabet.charAt(currentAlphabet.length() - 1);
				if (lastChar == 'Z') {
					// If the last character is 'z', replace it with 'a' and append a new 'A'
					String firstStr = currentAlphabet.substring(0, currentAlphabet.length() - 1);
					char firstChar = (char) (firstStr.charAt(0) + 1);
					return (firstChar) + "A";
				} else {
					// Otherwise, increment the last character by 1
					return currentAlphabet.substring(0, currentAlphabet.length() - 1) + (char) (lastChar + 1);
				}
			}
		} else if (currentAlphabet.length() == 3) {
			// Incrementing the last character of the string
			logger.debug("Project key Sub Area is Greater than AAA");
			char lastChar = currentAlphabet.charAt(currentAlphabet.length() - 1);
			if (lastChar == 'Z') {
				// If the last character is 'z', replace it with 'a' and append a new 'A'
				String firstStr = currentAlphabet.substring(0, currentAlphabet.length() - 1);
				char firstChar = (char) (firstStr.charAt(0) + 1);
				return (firstChar) + "A";
			} else {
				// Otherwise, increment the last character by 1
				return currentAlphabet.substring(0, currentAlphabet.length() - 1) + (char) (lastChar + 1);
			}
		} else {
			// Incrementing the last character of the string
			char lastChar = currentAlphabet.charAt(currentAlphabet.length() - 1);
			if (lastChar == 'Z') {
				// If the last character is 'z', replace it with 'a' and append a new 'A'
				String firstStr = currentAlphabet.substring(0, currentAlphabet.length() - 1);
				char firstChar = (char) (firstStr.charAt(0) + 1);
				return (firstChar) + "A";
			} else {
				// Otherwise, increment the last character by 1
				return currentAlphabet.substring(0, currentAlphabet.length() - 1) + (char) (lastChar + 1);
			}
		}
	}

	@Override
	public ResponseAsMessage deletedesignSubKeyArea(DeletedesignSubKeyAreaRequest deletedesignSubKeyAreaReq) {
		ResponseAsMessage returnMessage = new ResponseAsMessage();
		try {

			// List<KeyArea_ID> keyId =
			// iDesignDAO.getProjectSubExtnByProjSubId(deletedesignSubKeyAreaReq.getDskId());
			//
			// keyId.forEach(li -> {
			// iProjectDAO.deleteSubAreaExtn(li.getKeyId());
			//
			// });
			int indentCheck = iDesignDAO.indentpksaIdCheck(deletedesignSubKeyAreaReq.getPksaId());
			if (indentCheck > 0) {

				returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
				returnMessage.setResponseDataMessage("Failure");
				returnMessage.setResponseMessage(ResponseMessageMap.indentAllocated);
			} else {
				int updateIdentHdr = iDesignDAO.deleteDesignSunkey(deletedesignSubKeyAreaReq.getPksaId(),
						deletedesignSubKeyAreaReq.getTenantId());
				if (updateIdentHdr > 0) {
					returnMessage.setResponseCode(ResponseMessageMap.responseCodeOk);
					returnMessage.setResponseDataMessage("Success");
					returnMessage.setResponseMessage(ResponseMessageMap.successfulDeleted);
				} else {

					returnMessage.setResponseCode(ResponseMessageMap.failToupdateCode);
					returnMessage.setResponseDataMessage("Failure");
					returnMessage.setResponseMessage(ResponseMessageMap.failTodeleteMsg);
				}
			}

		} catch (Exception ex) {
			logger.error("deletedesignSubKeyArea error " + ex);
		}
		return returnMessage;
	}

	@Override
	public ResponseAsList getKeySubAreaByPKId(GetKeySubAreaByPKIdRequest getKeySubAreaByPKIdReq) {
		ResponseAsList returnL = new ResponseAsList();
		List<ProjectKeyAreaMstEntity> ka = iDesignDAO.getKeySubAreaByPKId(getKeySubAreaByPKIdReq);
			if(ka.size() ==0) {
			 ka = iDesignDAO.getKeySubAreaByIsdefault();	
			}
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}

	@Override
	public ResponseAsList getKeySubAreaDtl(ProductDtlDropDownRequest tentReq) {
		ResponseAsList returnL = new ResponseAsList();
		List<GetKeySubAreaDtlEntity> ka = iDesignDAO.getKeySubAreaDtl(tentReq.getPmHdrId(), tentReq.getTenantId());
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}

	@Override
	public ResponseAsList getProductBasedInventoryDtl(ProductBasedInventoryDtlRequest inventoryDtl) {

		ResponseAsList returnL = new ResponseAsList();
		List<ProductBasedInventoryDtlEntity> ka = iDesignDAO.getProductBasedInventoryDtl(inventoryDtl.getProductId(),
				inventoryDtl.getTenantId());
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}
	
	@Override
	public ResponseAsList getProductBasedPoDtl(ProductBasedInventoryDtlRequest inventoryDtlReq) {

		ResponseAsList returnL = new ResponseAsList();
		List<ProductBasedPoDtlEntity> ka = iDesignDAO.getProductBasedPoDtl(inventoryDtlReq.getProductId(),inventoryDtlReq.getPmHdrId(),
				inventoryDtlReq.getTenantId());
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}


	@Override
	public ResponseAsList getTasKTemplateHdr(GetTasKTemplateHdrRequest getTasKTemplateHdr) {
		ResponseAsList returnL = new ResponseAsList();
		List<GetTasKTemplateHdrEntity> ka = iDesignDAO.getTasKTemplateHdr(getTasKTemplateHdr.getTtCode(),
				getTasKTemplateHdr.getTcCode(),getTasKTemplateHdr.getTenantId());
		if (ka.size() > 0) {
			returnL.setResponseCode(ResponseMessageMap.responseCodeOk);
			returnL.setResponseMessage(ResponseMessageMap.success);
			returnL.setResponseData(ka);
		} else {
			returnL.setResponseCode(ResponseMessageMap.responseCodeNotOk);
			returnL.setResponseMessage(ResponseMessageMap.noRecord);
		}

		return returnL;
	}

	
}
