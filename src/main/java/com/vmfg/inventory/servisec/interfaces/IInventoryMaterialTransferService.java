package com.vmfg.inventory.servisec.interfaces;

import com.vmfg.design.request.TenantRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.inventory.request.AvailableProductsForTransferRequest;
import com.vmfg.inventory.request.AvailableQtyRequest;
import com.vmfg.inventory.request.BinDropDownRequest;
import com.vmfg.inventory.request.BinsForProductsRequest;
import com.vmfg.inventory.request.InsertMaterialTransferBatchRequest;
import com.vmfg.inventory.request.ProductDropDownRequest;
import com.vmfg.inventory.request.getQtyAvailLocRequest;
import com.vmfg.scm.request.PMInvReq;
import com.vmfg.scm.request.ProjectDtlRequest;

public interface IInventoryMaterialTransferService {

	ResponseAsList retrieveinventoryMaterial(ProjectDtlRequest projectdtlreq);

	ResponseAsList getProjectdropdown(TenantRequest tenanttreq);
	
	ResponseAsList getProjdtlOrdById(TenantRequest tenanttreq);
	
	ResponseAsList getProductdropdown(ProductDropDownRequest productdtldropdowntreq);

	ResponseAsList getAvailableProductsForTransfer(AvailableProductsForTransferRequest availableProductsForTransferReq);

	ResponseAsList getBins(BinDropDownRequest binDropDownRequest);

	ResponseAsList getBinsForProducts(BinsForProductsRequest binsForProductsRequest);
	
	ResponseAsList getLocationdropdown(TenantRequest tenanttreq);

	String getAvailableQty(AvailableQtyRequest availableqtyreq);

	ResponseAsMessage insertMaterialTransfer(InsertMaterialTransferBatchRequest insertmaterialreq);

	ResponseAsList getPmInv(PMInvReq invReq);

	ResponseAsList getPmInvDtl(PMInvReq invReq);

	ResponseAsList getQtyAvailableLocations(getQtyAvailLocRequest getQtyAvailLocReq);

}
