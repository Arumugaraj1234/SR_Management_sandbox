package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.DocumentManagementAccessEntity;
import com.vmfg.general.entity.DocumentManagementEntity;
import com.vmfg.general.entity.FileManagerDownloadEntity;
import com.vmfg.general.response.ResponseAsMessage;

public interface IDocumentManagementDAO {

	List<DocumentManagementEntity> getDocumentManagementDetails(String tENANT_ID, String eNQUIRY_ID, String pROJECT_D,
			String Emp_ID);

	FileManagerDownloadEntity documentDownloadDocFile(String tENANT_ID,  String rEFERNCE_ID);

	List<DocumentManagementAccessEntity> getdocumentManagementAccessDtl(String tENANT_ID, String dM_ID);

	ResponseAsMessage insertDocumentManagementAccessDtl(String tENANT_ID, String dM_ID, String dEPT_CODE);

	ResponseAsMessage deleteDocumentManagementAccessDtl(String dMA_ID);

	List<DocumentManagementEntity> getDocumentManagementDetailsById(String tENANT_ID, String refId, String stgCode);
	
	String getDeptByDmaId(String dmaId);
	
	int getDeptCountByDmId(String DmId,String dept);
	
	String getAccessDeptByDmId(String dmId,String tenantId);
	
	String getDepartmentDescByDepartmentCode(String department);

	String getEmpIdByDeptCode(String DEPT_CODE);

	ResponseAsMessage deleteDocumentManagementandAccessDtl(String dMA_ID);
}
