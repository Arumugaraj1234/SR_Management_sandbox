package com.vmfg.inventory.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.vmfg.design.dao.interfaces.IIndentUploadDAO;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.entity.GeneralLastSeqEntity;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.inventory.dao.interfaces.IInventoryMaterialTransferDao;
import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.entity.ProductBinEntity;
import com.vmfg.inventory.entity.ProductMasterEntity;
import com.vmfg.inventory.request.AvailableProductsForTransferRequest;
import com.vmfg.inventory.request.AvailableQtyRequest;
import com.vmfg.inventory.request.BinDropDownRequest;
import com.vmfg.inventory.request.BinsForProductsRequest;
import com.vmfg.inventory.request.InsertMaterialTransferBatchRequest;
import com.vmfg.inventory.request.MaterialTransferLineItem;
import com.vmfg.inventory.request.ProductDropDownRequest;
import com.vmfg.inventory.request.getQtyAvailLocRequest;
import com.vmfg.inventory.servisec.interfaces.IInventoryMaterialTransferService;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.request.PMInvReq;
import com.vmfg.scm.request.ProjectDtlRequest;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.FinanaceCodeGen;

@Service
public class InventoryMaterialTransferService implements IInventoryMaterialTransferService {

	private static final Logger logger = LoggerFactory.getLogger(InventoryMaterialTransferService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	IInventoryMaterialTransferDao iInventoryMaterialDao;
	@Autowired
	IIndentUploadDAO iIndentUploadDAO;

	@Override
	public ResponseAsList retrieveinventoryMaterial(ProjectDtlRequest projectdtlreq) {
		// TODO Auto-generated method stub

		ResponseAsList returnList = new ResponseAsList();
		String fromDate = projectdtlreq.getFromDate();
		String toDate = projectdtlreq.getToDate();
		String tenantId = projectdtlreq.getTenantId();
		List<InventoryMaterialTransferEntity> list = new ArrayList<>();
		try {

			list = iInventoryMaterialDao.retriveMaterial(fromDate, toDate, tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("retrieveinventoryMaterial service error " + e);
		}

		return returnList;
	}

	@Override
	public ResponseAsList getProjectdropdown(TenantRequest tenanttreq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		String tenantId = tenanttreq.getTenantID();
		try {
			List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
			list = iInventoryMaterialDao.getProjectdropdown(tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProjectdropdown service error " + e);
		}

		return returnList;
	}

	@Override
	public ResponseAsList getProjdtlOrdById(TenantRequest tenanttreq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		String tenantId = tenanttreq.getTenantID();
		try {
			List<ProjectDtlsEntity> list = new ArrayList<ProjectDtlsEntity>();
			list = iInventoryMaterialDao.getProjdtlOrdById(tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProjectdropdown service error " + e);
		}

		return returnList;
	}

	@Override
	public ResponseAsList getProductdropdown(ProductDropDownRequest productdtldropdowntreq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		String tenantId = productdtldropdowntreq.getTenantId();
		String pmhdrId = productdtldropdowntreq.getPmHdrId();
		try {
			List<ProductMstDropDownEntity> list = new ArrayList<ProductMstDropDownEntity>();
			list = iInventoryMaterialDao.getProductdropdown(pmhdrId, tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getProductdropdown service error " + e);
		}

		return returnList;
	}
	@Override
	public ResponseAsList getAvailableProductsForTransfer(AvailableProductsForTransferRequest availableProductsForTransferReq) {
		ResponseAsList returnList = new ResponseAsList();
		String tenantId = availableProductsForTransferReq.getTenantId();
		String pmHdrId = availableProductsForTransferReq.getPmHdrId();
		String locationCode = availableProductsForTransferReq.getLocationCode();
		try {
			List<ProductMstDropDownEntity> list = iInventoryMaterialDao.getAvailableProductsForTransfer(pmHdrId, locationCode, tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			logger.error("getAvailableProductsForTransfer service error " + e);
		}
		return returnList;
	}
	@Override
	public ResponseAsList getBins(BinDropDownRequest binDropDownRequest) {
	    ResponseAsList returnList = new ResponseAsList();
	    String tenantId = binDropDownRequest.getTenantId();
	    String pmHdrId = binDropDownRequest.getPmHdrId();
	    String productId = binDropDownRequest.getProductId();
	    try {
	    	List<String> list = iInventoryMaterialDao.getBins(tenantId, pmHdrId, productId);
	        
	        if (list.size() > 0) {
	            returnList.setResponseData(list);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
	            returnList.setResponseMessage(ResponseMessageMap.success);
	        } else {
	            returnList.setResponseData(list);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
	            returnList.setResponseMessage(ResponseMessageMap.noRecord);
	        }
	    } catch (Exception e) {
	        logger.error("getBins service error " + e);
	    }

	    return returnList;
	}
	@Override
	public ResponseAsList getBinsForProducts(BinsForProductsRequest binsForProductsRequest) {
	    ResponseAsList returnList = new ResponseAsList();
	    String tenantId = binsForProductsRequest.getTenantId();
	    String pmHdrId = binsForProductsRequest.getPmHdrId();
	    List<String> productIds = binsForProductsRequest.getProductIds();
	    try {
	        List<ProductBinEntity> list = iInventoryMaterialDao.getBinsForProducts(tenantId, pmHdrId, productIds);

	        if (list.size() > 0) {
	            returnList.setResponseData(list);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
	            returnList.setResponseMessage(ResponseMessageMap.success);
	        } else {
	            returnList.setResponseData(list);
	            returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
	            returnList.setResponseMessage(ResponseMessageMap.noRecord);
	        }
	    } catch (Exception e) {
	        logger.error("getBinsForProducts service error " + e);
	    }

	    return returnList;
	}
	@Override
	public ResponseAsList getLocationdropdown(TenantRequest tenanttreq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		String tenantId = tenanttreq.getTenantID();
		try {
			List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
			list = iInventoryMaterialDao.getLocationdropdown(tenantId);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getLocationdropdown service error " + e);
		}

		return returnList;
	}
	
	@Override
	public ResponseAsList getQtyAvailableLocations(getQtyAvailLocRequest getQtyAvailLocReq) {
		// TODO Auto-generated method stub
		ResponseAsList returnList = new ResponseAsList();
		List<LocationDropDownEntity> list = new ArrayList<LocationDropDownEntity>();
		try {
			list = iInventoryMaterialDao.getQtyAvailableLocations(getQtyAvailLocReq.getProductId(),getQtyAvailLocReq.getTenantID());
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("getQtyAvailableLocations service error " + e);
		}

		return returnList;
	}

	@Override
	public String getAvailableQty(AvailableQtyRequest availableqtyreq) {
		// TODO Auto-generated method stub
		String productId = availableqtyreq.getProductID();
		String location = availableqtyreq.getFrmLocationCode();
		String tenantId = availableqtyreq.getTenantID();
		String qty = "";
		try {
			qty = iInventoryMaterialDao.getAvailableQty(productId, location, tenantId);
		} catch (Exception e) {
			logger.error("getAvailableQty service error " + e);
		}
		return qty;
	}

	@Override
	@Transactional
	public ResponseAsMessage insertMaterialTransfer(InsertMaterialTransferBatchRequest insertmaterialreq) {
		String transferDate = CommonMethod.getCurrentDate();
		String createdOn = CommonMethod.getCurrentDateTime();

		String fromPmHdrId = insertmaterialreq.getFromPmHdrId();
		String toPmHdrId = insertmaterialreq.getToPmHdrId();
		String fromInventoryLocationCode = insertmaterialreq.getFromInventoryLocationCode();
		String toInventoryLocationCode = insertmaterialreq.getToInventoryLocationCode();
		String createdBy = insertmaterialreq.getCreatedBy();
		String reason = insertmaterialreq.getRemark();
		String tenantId = insertmaterialreq.getTenantId();
		String transferGroupId = UUID.randomUUID().toString();

		ResponseAsMessage responseMsg = new ResponseAsMessage();
		List<MaterialTransferLineItem> items = insertmaterialreq.getItems();
		int expectedCount = items == null ? 0 : items.size();
		int totalInserted = 0;

		try {
			for (MaterialTransferLineItem item : items) {
				String productId = item.getProductId();
				String productCode = item.getProductCode();
				String transferQuantity = item.getTransferQuantity();
				String fromBin = item.getFromBin();
				String toBin = item.getToBin();

				GeneralLastSeqEntity gen = FinanaceCodeGen.genCodeForTrans(CommonMethod.getCurrentDateTime(), tenantId,
						"inventroy_material_transfer", "5", jdbcTemplate, 1, 0, null, 0);
				String avgValue = iInventoryMaterialDao.getAvgUnitValue(fromPmHdrId, toPmHdrId, productId);
				iInventoryMaterialDao.updateUnitVal(avgValue, toPmHdrId, productCode);
				BigDecimal transferQty = new BigDecimal(transferQuantity);
				BigDecimal avgQtyValue = new BigDecimal(avgValue);
				BigDecimal overallQty = transferQty.multiply(avgQtyValue);

				ProductMasterEntity prodMstResp = null;
				int countProdMstCountCheck;
				if (fromPmHdrId.equals(toPmHdrId)) {
					// Same project on both sides: the destination product is the source product,
					// no need to look it up or copy it - avoids a false-negative "not found" (and
					// a resulting duplicate-row insert attempt) when PRODUCT_CATEGORY isn't 'CN'.
					countProdMstCountCheck = Integer.parseInt(productId);
				} else {
					// count check for toPmHdrId with productCode if exist get ProductId else Insert
					countProdMstCountCheck = iIndentUploadDAO.checkProductCodeInProdMst(productCode, item.getDesc(), item.getSpec(),
							tenantId, toPmHdrId, "CN");
					if (countProdMstCountCheck == 0) {
						prodMstResp = iInventoryMaterialDao.getProductMstData(fromPmHdrId, toPmHdrId, productId);
						if (prodMstResp != null) {
							// The re-check below requires PRODUCT_CATEGORY='CN'; the source row's
							// category (copied as-is otherwise) may be NULL/different, which would
							// make the freshly-inserted row permanently fail its own verification.
							prodMstResp.setProductCategory("CN");
							int insVal = iInventoryMaterialDao.insertProductMstData(prodMstResp, fromPmHdrId, toPmHdrId);
							if (insVal > 0) {
								countProdMstCountCheck = iIndentUploadDAO.checkProductCodeInProdMst(productCode, item.getDesc(), item.getSpec(),
										tenantId, toPmHdrId, "CN");
							}
						}
					}
					if (countProdMstCountCheck == 0) {
						throw new RuntimeException("Unable to resolve destination product for productCode=" + productCode);
					}
				}

				// Updating the BIN value in the product_mst table for the given Product ID
				iInventoryMaterialDao.updateProductBin(countProdMstCountCheck, toBin);

				int insert = iInventoryMaterialDao.insertMaterialTransfer(Integer.toString(countProdMstCountCheck), fromPmHdrId, toPmHdrId,
						fromInventoryLocationCode, toInventoryLocationCode, transferQuantity, transferDate, createdOn,
						createdBy, reason, gen.getSeq(), gen.getFinainceId(), gen.getEnquiryCode(), tenantId, avgQtyValue, overallQty,
						fromBin, toBin, transferGroupId);

				if (insert <= 0) {
					throw new RuntimeException("Failed to insert material transfer row for productCode=" + productCode);
				}

				int productId1 = iIndentUploadDAO.checkProductCodeInProdMst(productCode, item.getDesc(), item.getSpec(),
						tenantId, toPmHdrId, "CN");

				int fromInvUpdate = CommonMethod.updateProductInvDtl(fromPmHdrId, productId,
						fromInventoryLocationCode, transferQty, "Subraction", "ITTC0001", gen.getEnquiryCode(), createdBy,
						createdOn, tenantId, jdbcTemplate);
				if (fromInvUpdate > 0) {
					String toProductId = productId1 == 0 ? productId : String.valueOf(productId1);
					CommonMethod.updateProductInvDtl(toPmHdrId, toProductId, toInventoryLocationCode, transferQty, "",
							"ITTC0001", gen.getEnquiryCode(), createdBy, createdOn, tenantId, jdbcTemplate);
				}

				totalInserted += insert;
			}

			responseMsg.setResponseCode(ResponseMessageMap.responseCodeOk);
			responseMsg.setResponseMessage(ResponseMessageMap.successUpdated);
			responseMsg.setResponseDataMessage(String.valueOf(totalInserted));
		} catch (Exception e) {
			logger.error("insertMaterialTransfer service error " + e);
			// Force rollback of everything inserted so far in this loop (all-or-nothing),
			// while still returning a real error response instead of letting the exception
			// propagate (the controller's own catch would otherwise discard this message
			// and return a blank ResponseAsMessage).
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			responseMsg.setResponseCode(ResponseMessageMap.failToupdateCode);
			responseMsg.setResponseDataMessage("Fail to update");
			responseMsg.setResponseMessage(ResponseMessageMap.failToupdateMsg);
		}
		return responseMsg;
	}

	@Override
	public ResponseAsList getPmInv(PMInvReq invReq) {

		ResponseAsList returnList = new ResponseAsList();
		List<InvProdEntity> list = new ArrayList<>();
		try {

			list = iInventoryMaterialDao.getPmInv(invReq);
			if (list.size() > 0) {
//				for(int i =0;i<list.size();i++) {
//				List<PocodeAndUnitRate> pocodeAndUnitRate	= iInventoryMaterialDao.PocodeAndUnitRate(list.get(i).getProdCode(),list.get(i).getProjectId());
//				if(pocodeAndUnitRate.size()>0) {
//				list.get(i).setPoCode(pocodeAndUnitRate.get(0).getPoCode());
//			//	list.get(i).setUnitRate(pocodeAndUnitRate.get(0).getUnitRate());
//				}
//				}
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception e) {
			logger.error("retrieveinventoryMaterial service error " + e);
		}

		return returnList;
	}

	@Override
	public ResponseAsList getPmInvDtl(PMInvReq invReq) {

		ResponseAsList returnList = new ResponseAsList();
		List<InvProdEntity> list = new ArrayList<>();
		try {

			list = iInventoryMaterialDao.getPmInvDtl(invReq);
			if (list.size() > 0) {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeOk);
				returnList.setResponseMessage(ResponseMessageMap.success);
			} else {
				returnList.setResponseData(list);
				returnList.setResponseCode(ResponseMessageMap.responseCodeNotOk);
				returnList.setResponseMessage(ResponseMessageMap.noRecord);
			}

		} catch (Exception e) {
			logger.error("retrieveinventoryMaterial service error " + e);
		}

		return returnList;
	}

}
