package com.vmfg.design.dao.interfaces;

import java.util.List;

import com.vmfg.design.entity.GetKeySubAreaDtlEntity;
import com.vmfg.design.entity.GetTasKTemplateHdrEntity;
import com.vmfg.design.entity.ProductBasedInventoryDtlEntity;
import com.vmfg.design.entity.ProductBasedPoDtlEntity;
import com.vmfg.design.entity.ProductMstDropDownEntity;
import com.vmfg.design.entity.ProductMstEntity;
import com.vmfg.design.entity.ProjectKeyAreaMstEntity;
import com.vmfg.design.entity.getPoDetailByIndentDtlEntity;
import com.vmfg.design.request.GetKeySubAreaByPKIdRequest;
import com.vmfg.design.request.ProductDtlDropDownRequest;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.design.response.KeyArea_ID;
import com.vmfg.project.entity.ProjectSubAreaExtnEntity;

public interface IDesignDAO {

	List<DesignHdr> getDesignHdr(String fromDate, String toDate, String customer, String processId, String empId,
			String tenantID, String designID, String projectId);

	String getTaskPlanned(String designID, String dept, String tenantID, String completionStatus);

	String getIndentPlanned(String designID, String dept, String tenantID, String completionStatus);

	List<ProjectKeyAreaMstEntity> getKeyArea(ProductDtlDropDownRequest tentReq);

	List<ProjectKeyAreaMstEntity> getKeySubArea(ProductDtlDropDownRequest tentReq);

	List<ProjectKeyAreaMstEntity> getKeySubAreaByPKId(GetKeySubAreaByPKIdRequest getKeySubAreaByPKIdReq);
	
	List<ProjectKeyAreaMstEntity> getKeySubAreaByIsdefault();

	List<GetKeySubAreaDtlEntity> getKeySubAreaDtl(String pmHdrId, String tenantId);

	List<ProductMstDropDownEntity> getAllProductsByPmHdrId(String pmHdrId, String tenantId, String isQty);

	List<ProductMstEntity> getAllProductsByPmHdrId(String pmHdrId, String tenantId);

	int insertDesignSubKey(String deHdrId, String pskId, String tenantId, String pkaId);

	int deleteDesignSunkey(String DskId, String tenantId);

	int indentpksaIdCheck(String pksaid);

	String getIndentPlannedByProject(String projectId, String dept, String tenantID, String completionStatus);

	int updateDesignSubKey(String dskId, String deHdrId, String pskId, String pkaId);

	String getIndentPlannedByProjectAndsts(String projId, String tenantID, String completionStatus);

	List<KeyArea_ID> getProjectSubExtnByProjSubId(String dskId);

	List<ProjectSubAreaExtnEntity> getProjectExtnByProjSubId(String pkaId);

	List<ProductBasedInventoryDtlEntity> getProductBasedInventoryDtl(String productId, String tenantId);
	
	List<GetTasKTemplateHdrEntity>getTasKTemplateHdr(String ttCode,String tcCode,String tenantId);

	List<getPoDetailByIndentDtlEntity> getPoDetailByIndentDtlRequest(String indentDtlId, String tenantId);

	List<ProductBasedPoDtlEntity> getProductBasedPoDtl(String productId, String pmHdrId, String tenantId);
}
