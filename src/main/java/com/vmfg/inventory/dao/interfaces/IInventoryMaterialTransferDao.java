package com.vmfg.inventory.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.inventory.entity.InvProdEntity;
import com.vmfg.inventory.entity.InventoryMaterialTransferEntity;
import com.vmfg.inventory.entity.LocationDropDownEntity;
import com.vmfg.inventory.entity.PocodeAndUnitRate;
import com.vmfg.inventory.entity.ProductBinEntity;
import com.vmfg.inventory.entity.ProductMasterEntity;
import com.vmfg.scm.entity.ProjectDtlsEntity;
import com.vmfg.scm.request.PMInvReq;

public interface IInventoryMaterialTransferDao {

	List<InventoryMaterialTransferEntity> retriveMaterial(String fromDate, String toDate, String tenantId);

	List<ProjectDtlsEntity> getProjectdropdown(String tenantId);

	List<ProjectDtlsEntity> getProjdtlOrdById(String tenantId);

	List<ProductMstDropDownEntity> getProductdropdown(String pmhdrId, String tenantId);

	List<ProductMstDropDownEntity> getAvailableProductsForTransfer(String pmHdrId, String locationCode, String tenantId);

	List<String> getBins(String tenantId, String pmHdrId, String productId);

	List<ProductBinEntity> getBinsForProducts(String tenantId, String pmHdrId, List<String> productIds);

    List<LocationDropDownEntity> getLocationdropdown(String tenantId);

	String getAvailableQty(String productId, String location, String tenantId);

	int insertMaterialTransfer(String productId, String fromPmHdrId, String toPmHdrId, String fromInventoryLocationCode,
			String toInventoryLocationCode, String transferQuantity, String transferDate, String createdOn,
			String createdBy, String reason, int seq, String finainceId, String enquiryCode, String tenantId, BigDecimal avgQtyValue, BigDecimal overallQty,String fromBin, String toBin, String transferGroupId);
	
	int updateProductBin(int productId, String toBin);
    
	List<InvProdEntity> getPmInv(PMInvReq invReq);

	List<InvProdEntity> getPmInvDtl(PMInvReq invReq);
	
	List<PocodeAndUnitRate>PocodeAndUnitRate(String productCode,String pmHDrId);

	List<LocationDropDownEntity> getQtyAvailableLocations(String productId, String tenantId);

	String getBinByprodCode(String productId, String pmHdrId, String tenantId);

	String getAvgUnitValue(String fromPmHdrId, String toPmHdrId, String productCode);

	int updateUnitVal(String avgValue, String toPmHdrId, String productCode);

	ProductMasterEntity getProductMstData(String fromPmHdrId, String toPmHdrId, String productCode);

	int insertProductMstData(ProductMasterEntity prodMstResp, String fromPmHdrId, String toPmHdrId);

	int getProductMstCountCheck(String toPmHdrId, String productCode,String tenantId);


}
