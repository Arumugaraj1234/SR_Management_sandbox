package com.vmfg.assembly.dao.interfaces;

import java.util.List;

import com.vmfg.assembly.entity.MaterialReturnAcceptEntity;
import com.vmfg.assembly.entity.MaterialReturnDtlAcceptEntity;
import com.vmfg.assembly.entity.MrHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveMReturnDtlByHdrEntity;

public interface IAssemblyReturnDAO {

	List<MrHdrRetrieveEntity> mrHdrRetrieve(String hdrId, String tenantId);

	int insertMaterialReturnDtl(int responseMrHdrId, String productId, String qty, String tenantId, String msHdrId,
			String msName);

	List<RetrieveMReturnDtlByHdrEntity> retrieveMreturnDtlByHdr(String hdrId, String tenantId);

	List<RetrieveMReturnDtlByHdrEntity> retrieveApprovedGroupReturnsByProject(String pmHdrId, String tenantId);

	int cancelMaterialReturnHdr(String hdrId, String tenantId,String seqStatus,String seqNo);

	List<MaterialReturnAcceptEntity> materialReturnAccept(String mrhId, String tenantId);

	int insertMaterialReturnHdr(String pmHdrId, String mrName, String tenantId, String createdBy,
			String seqNo, String seqStatus);

	int updateStatusInMaterialReturnHdr(String mrhId, String currentSeq, String seqStatus,String isCompleted);

	int insertReturnRemarks(String mrhId, String string, String currentSeq, String seqStatus, String remarks,
			String empId, String tenantId);

	List<MaterialReturnDtlAcceptEntity> ApproveMreturnDtls(String dtlId, String tenantId);

	int approveMaterialReturnHdr(String mrHdrId, String tenantId, String docStatus, String seqNo, String empId);

	String getSeqandStatus(String lastSeq, String docType, String tenantId);

	int ApproveDtls(String mrDtlId, String tenantId);

}
