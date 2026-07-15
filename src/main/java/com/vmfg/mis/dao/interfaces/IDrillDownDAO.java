package com.vmfg.mis.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.vmfg.inventory.entity.InvProdDrillEntity;
import com.vmfg.inventory.entity.NoOfPoDrillEntity;
import com.vmfg.mis.entity.DelayedEntity;
import com.vmfg.mis.entity.DrilldownEntity;
import com.vmfg.mis.entity.InitialScopValuePOEntity;

public interface IDrillDownDAO {


	List<DrilldownEntity> getIndentByNotAvailablePOList(String tenantId, String projectId, String pmId, String empId, String monthYear);

	List<InitialScopValuePOEntity> getPoInitalValueList(String pmId, String tenantId, String monthYear);

	List<InvProdDrillEntity> getInventoryValueDrillList(String pmId, String tenantId, String projectId);

	List<DelayedEntity> getDelayedIndentList(String tenantId, String projectId, String pmId, String empId, String monthYear);

	List<NoOfPoDrillEntity> getNumberOfPoDrillList(String projId, String tenantId, String empId, String pmId,
			String monthYear);

	Map<String, String> getPjsCheck(List<String> indentDtlIds, String tenantId);

	Map<String, String> getIndentCheck(List<String> indentDtlIds, String tenantId);

	Map<String, String> getPoCheck(List<String> indentDtlIds, String tenantId);

	List<DrilldownEntity> getIndentByInventoryStock(String tenantId, String projectId, String pmId, String empId,
			String monthYear);

	Map<String, String> getAssignedBy(List<String> indentDtlIds, String tenantId);

	String getPrimaryDocFlagVal(String empId, String pmId, String tenantId);

}
