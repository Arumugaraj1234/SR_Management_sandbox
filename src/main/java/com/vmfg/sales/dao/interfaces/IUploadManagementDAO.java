package com.vmfg.sales.dao.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.sales.entity.DocumentAppStatusDtlEntity;
import com.vmfg.sales.entity.DocumentManagementTblEntity;
import com.vmfg.sales.entity.FileUploadConfigtblEntity;
import com.vmfg.sales.entity.ApprovedDocEntity;
import com.vmfg.sales.entity.ChangeRequestHdrInfoEntity;

public interface IUploadManagementDAO {

	List<DocumentManagementTblEntity> getDocDlsByDmId(String dmId, String tenantId, String approve);

	List<DocumentAppStatusDtlEntity> getDocStatusList(String dmId, String tenantId);

	List<ApprovedDocEntity> getDocDtlsByCombination(String enquiryId, String projectId, String documentName,
			String refId, String stageCode, String tenantId);

	int getLatestVersionbycomb(String enquiryId, String tenantId, String documentName, String refId);

	int getCountByComb(String enquiryId, String tenantId, String documentName, String refId);

	List<FileUploadConfigtblEntity> getFileDtlsByDocTypeCode(String docTypeCode, String tenantId);

	int getFileDtlCountByDocTypeCode(String docTypeCode, String tenantId);

	String getDepCodeByEmpId(String empId, String tenantId);
	
	String getDocumentNameByDmId(int dmId, String tenantId);

	int insertDocMagAccessDtl(int newDmId, String depCode, String tenantId);

	List<DocumentStatusMstEntity> getSeqAndStatusByDocTypeCode(String docTypeCode, String tenantId);

	String getDocTypeCodeByDocDesc(String docType, String tenantId);

	int insertDocAppStatusDtl(int newDmId, String docAppSeq, String tenantId, String seqStatus, String empId);

	String getDesigCodeByEmpId(String empId, String tenantId);

	int getCurrentSeqbyDmId(String dmId, String tenantId);

	int getApprovebtnEnableStatus(String designCode, String tenantId, String docTypeCode, String string);

	List<DocumentStatusMstEntity> getNextSeqandStatus(int currentSeq, String docType, String tenantId);

	int checkForNextSeq(int currentSeq, String docType, String tenantId);

	int updateNextSeqInDmTbl(String dmId, String tenantId, String currSequence);

	int updateLatestVersion(String tenantId, String dmId, String uploadDocType,String enqId);

	int updateApprAndLatest(String tenantId, String dmId);

	int insertNewFileDtl(MultipartFile file, String tenantId, int dmId, String uploadDocType, String empId, int version,
			String documentType, String type, String refId);

	String getUploadDocTypeByDmId(String dmId, String tenantId);

	List<DocumentStatusMstEntity> getPrevSeqandStatus(int currentSeq, String docType, String tenantId);

	String getMasterDoc(String depCode, String tenantId ,String pmId);

	int insertDocumentDtls(String enquiryId, String projectId, String documentName, String refId, String stageCode,
			String uploadDocType, int version, String tenantId, String remarks, String docApprSeq, String lastSeq,
			String docType);

	List<ApprovedDocEntity> getApprovedDocDtl(String enquiryId, String stageCode, String approved, String tenantId,
			String docType);
	
	
	String getDocTypeCodeBystgAndpmId(String stgCode,String pmId);
	
	String getCurrStageByRefId(String masterId);

	int getChangeRequestInfoCheck(String sbcHdrId, String tenantId);

	List<ChangeRequestHdrInfoEntity> getChangeRequestInfo(String sbcHdrId, String tenantId);

	List<DocumentManagementTblEntity> getAccDocDlsByDmId(String dmId, String tenantId, String approve);

}
