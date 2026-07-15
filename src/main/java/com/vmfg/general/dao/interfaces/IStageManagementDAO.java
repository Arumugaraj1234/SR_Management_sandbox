package com.vmfg.general.dao.interfaces;

import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.GetComponentDtls;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.general.entity.ProcessConfigEntity;
import com.vmfg.general.entity.ProjectDueDateEntity;
import com.vmfg.general.entity.ProjectWbsInitiationMst;
import com.vmfg.general.entity.StatusDtlEntity;
import com.vmfg.general.request.InitiateProcessRequest;

public interface IStageManagementDAO {

	String getcurrentEnquiryStage(String referenceId, String tenantId);

	String getEnquiryDtlId(String referenceId);

	String getcurrentEnquiryStatus(String referenceId, String tenantId, String tableName);

	String getcurrentEnquiryStageSeqMstTable(String referenceId, String tenantId, String tableName, String tablecolumn);

	String getcurrentStageSeqMstTable(String referenceId, String tableName, String slaveColumn);

	String getcurrentMstTblStage(String referenceId, String tenantId, String MstName, String mstColumnId);

	int getPreStgValCheck(String proccessCode, String seq,String tenantId);

	List<ProcessConfigEntity> getprocessDtlcurrentSeq(String proccessCode, String seq);

	List<GetstageprocessDtlEntity> getprocessDtlBySeq(String processCode, String seq, String isVisiable,
			String tenantId);

	List<GetstageprocessDtlEntity> getVisibleAllprocessDtl(String processCode, String visibleAll, String tenantId);

	List<ProcessConfigEntity> getNextprocessStaDtlBySeq(String processCode, String seq, String isVisiable,
			String tenantId);

	List<DocumentStatusMstEntity> getNextSeqbatchDtl(String referenceDoc, String seq, String tenantId);

	List<DocumentStatusMstEntity> getDocCurrentSeqDtl(String referenceDoc, String seq, String tenantId);

	List<DocumentStatusMstEntity> getDocDtlcurrentSeq(String referenceDoc, String seq, String tenantId);

	String getRefTableNameByDocTyp(String docType, String tenantId);

	String getMstTableNameByDocTyp(String docType, String tenantId);

	String getStatusTableNameByDocTyp(String docType, String tenantId);

	String getDocstsBydocseqAtype(String referenceDoc, String seq, String tenantId);

	int updateProcessDtlStsAndCode(String referenceId, String stsSeq, String stsCode, String refTableName,
			String refCloumn);

	int updateProcessHdrStsAndCode(String referenceId, String stsSeq, String stsCode, String mstTableName,
			String mstCoulmnName,String empId);

	int updateProcessHdrStgAndCode(String referenceId, int stgSeq, String stgCode, String mstTableName,
			String mstCoulmnName,String empId);

	int updateProcesStatusDtl(String referenceId, String stsSeq, String stsCode, String statusTableName, String empId,
			String docType, String tenantId, String remarks);

	String getNextStgDtl(String process, String seq, String tenantId);

	String getProcessLifeCycleCurrSeq(String processCode, String status, String tenantId);

	List<DocumentTypeMstEntity> getDocTypeMstDtl(String DocType, String pmId);

	String getDistinctMstTableNameDtlId(String pmId, String tenantId);

	List<DocumentTypeMstEntity> getDocTypeMstDtlByStage(String stgCode, String pmId, String tenantId);

	String getEmpDesinationCode(String empId, String tenantId);

	int empApproveCheck(String desiCode, String docType, String currSeq, String tenantId);

	String getSalesEnqDtlStatus(String slaveId, String refTableName);

	int getisEditableStatus(String currSeq, String docType, String pmId, String tenantId);

	String getDistinctMstTableId(String pmId, String tenantId);

	List<DocumentTypeMstEntity> getDocTypeMstByDoc(String docType);

	String getcurrentMstTblStatus(String referenceId, String tenantId, String MstName, String mstColumnId);

	List<DocumentStatusMstEntity> getFirstOrLastSeqDocDtl(String referenceDoc, String Seqtype, String tenantId);

	List<StatusDtlEntity> getStatusDtl(String docType, String referenceId, String tenantId, String statusTableName);

	List<ProjectWbsInitiationMst> getPMFromDept(InitiateProcessRequest initiateProcessReq);

	String getMasterIdBySlaveId(String slaveId, String tableName, String tenantId, String tableColumnName);

	String getNextApprDesigByDocType(String docType, String currentSeq, String tenantId);

	List<DocumentStatusMstEntity> getNextSeqbatchDtlByDesig(String referenceDoc, String seq, String tenantId,
			String Desig);

	int GenerateAndUpdateProjectCode(String hdrId, String tenantId, String projectCode);

	int updateInitiationDate(String initiationDate, String seqStatus, String seq, String pmHdrId);

	int updateProjectDtl(String seqStatus, String seq, String pmHdrId);

	String checkMasterInfo(String pmId, String refId, String tenantID,int getPmhdrIdFlag);

	String insertMasterInfo(String pmId, String enqId, String tenantID, String dueDate, String empID, String startDate, int getPmhdrIdFlag);

	String getChildPMID(String pmId, String tenantId);

	List<ProjectWbsInitiationMst> getPMFromPMID(String PMID, String tenantId);

	List<DocumentStatusMstEntity> getDocDtlcurrentSeqByDocGrp(String referenceDoc, String seq, String tenantId,
			String docGrp);

	String getNextApprDesigByDocTypeByDocGrp(String docType, String currentSeq, String tenantId, String docGrp);
	
	
	String getprojectCode(String pmId,String refId);

	int getMstCompletedVal(String docType, String seq, String tenantId);

	int updateMstTblIsCompleted(String mstId, String mstTableName, String mstColName);

	String getPmHdrIdByEnqId(String enqId);

	int updateProjectDueDate(String dueDate, String empId, String pmHdrId, String reason, String tenantId);

	List<ProjectDueDateEntity> getProjectDueDates(String pmHdrId, String tenantId);

	void updatePmDueDate(String pmHdrId, String dueDate);

	int udpateDueDate(String tableName,String startDate,String dueDate,String tenantId,String pmHdrId);
	
	int udpatestartAndEndDate(String tableName,String startDate,String dueDate,String tenantId,String pmHdrId);

	String getNextApprDesigByDocGrp(String docType, String currentSeq, String tenantId, String docGroup);

	String getStageCodeForPmId(String pmId, String docDesc, String tenantId);

	List<GetComponentDtls> getComponentNameForPmId(String pmId, String stgCode, String tenantId);

	String getSaleEnqRefId(String referenceId, String tenantId);

	int getProjectCode(String projectCode, String tenantId);


}
