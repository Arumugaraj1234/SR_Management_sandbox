package com.vmfg.design.services.interfaces;

import java.util.List;

import com.vmfg.design.request.DeletedesignSubKeyAreaRequest;
import com.vmfg.design.request.DesignRequest;
import com.vmfg.design.request.GetKeySubAreaByPKIdRequest;
import com.vmfg.design.request.GetTasKTemplateHdrRequest;
import com.vmfg.design.request.ProductBasedInventoryDtlRequest;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.request.UpdatedesignSubKeyAreaRequest;
import com.vmfg.design.request.getPoDetailByIndentDtlRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDesignService {

	ResponseAsList getDesignHdr(DesignRequest designReq);

	ResponseAsList getKeyArea(ProductDtlDropDownRequest tentReq);

	ResponseAsList getKeySubArea(ProductDtlDropDownRequest tentReq);
	
	ResponseAsList getKeySubAreaDtl(ProductDtlDropDownRequest tentReq);
	
	
	ResponseAsList getKeySubAreaByPKId(GetKeySubAreaByPKIdRequest getKeySubAreaByPKIdReq);
	
	ResponseAsList getAllProductsByPmHdrId(ProductDtlDropDownRequest productDtlDropDownReq);
	
	ResponseAsList getAllProductsDtl(ProductDtlDropDownRequest productDtlDropDownReq);
	
	
	ResponseAsMessage updatedesignSubKeyArea(List<UpdatedesignSubKeyAreaRequest> updatedesignSubKeyAreaReq);
	ResponseAsMessage deletedesignSubKeyArea(DeletedesignSubKeyAreaRequest deletedesignSubKeyAreaReq);

	ResponseAsList getProductBasedInventoryDtl(ProductBasedInventoryDtlRequest inventoryDtl);
	
	ResponseAsList getTasKTemplateHdr(GetTasKTemplateHdrRequest getTasKTemplateHdrReq);

	ResponseAsList getPoDetailByIndentDtl(getPoDetailByIndentDtlRequest indentDtlIdReq);

	ResponseAsList getProductBasedPoDtl(ProductBasedInventoryDtlRequest inventoryDtlReq);
}
