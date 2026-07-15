package com.vmfg.design.dao.interfaces;

import java.util.List;

import com.vmfg.design.entity.*;
import org.springframework.web.multipart.MultipartFile;

import com.vmfg.design.response.KeyAreaIndentId;
import com.vmfg.design.response.KeySubArea;

public interface IChangeRequestDAO {

	List<ChangeRequestHdrEntity> ChangereqHdrList(String pmId,String tenantId);
	String designationDesc(String desigCode);

	int insertChangeRequestHdr(String crNo,String crCode,String deHdId,String pmHdrId,
String initiatedBy,String crDate,String productCode,String pkId,String pskId,String requestDetail,String nextApprovingDesig,String status,String seq,String updateDrawNo,String udateDrawRevNo,String createdBy );

	int updateChangeRequestHdr(String deHdId,String pmHdrId,
String initiatedBy,String crDate,String productCode,String pkId,String pskId,String requestDetail,String nextApprovingDesig,String updateDrawNo,String udateDrawRevNo,String lastUpdatedBy,String tenantId,String crhdrId);
	
	int updateChangeReqHdrStatus(String crHdrId, String seq, String seqStatus, String nextApprdesig, String empId);
	int getDmIdVal(String cHdrId,String tenantId);
	
	int updateDocManagementByDmId(String dmId, String fileName);
	int updateFileByDmId(MultipartFile file, String tenantId, String dmId, String uploadDocType, String documentType,
			String type);
	int insertChangeRequestDtl(String crHdrId, String designedComment, String tenantId, String empId);
	int updateChangeRequestDtl(String crDtlId, String crHdrId, String designedComment, String tenantId, String empId);
	String getStatusDescByStatusCode(String docStatusCode, String tenantId);
	List<ChangeRequestDtlEntity> ChangereqDtlList(String ceHdrid, String tenantId);
	
	List<KeyAreaIndentId> getKeyAreaDtls(String productCode, String masterId, String tenantId);

	List<KeySubArea> getSubKeyAreaDtls(String productCode, String masterId, String tenantId,String indentId);

	int getRevisionNoCount(String productCode, String masterId, String tenantId);
	List<ChangeRequestIndentEntity> getChangeRequestByIndentId(String projectId, String tenantId);
	List<ChangeRequestIndentDtlEntity> getChangeRequestByIndentDtlId(String indentId,
			String tenantId);




	IndentPartDetailsEntity getIndentDetailsByIndentCode(Integer indentId);
}
