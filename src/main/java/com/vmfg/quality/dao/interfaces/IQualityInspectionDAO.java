package com.vmfg.quality.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.quality.entity.QIStatusEntity;
import com.vmfg.quality.entity.QiCaEntity;
import com.vmfg.quality.entity.QualityInsReqEntity;
import com.vmfg.quality.entity.QualityInspectionDtlEntity;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.quality.entity.RetrieveQualitInspectionEntity;
import com.vmfg.quality.request.UpdateVendorRatingRequest;

public interface IQualityInspectionDAO {


	List<QualityInspectionHdrEntity> getQIConfigHdrList(String qicName, String tenantId);

	List<QualityInspectionDtlEntity> getQIConfigDtls(String qicHdrId);

	List<QualityInspectionHdrEntity> getQiHdrList(String qiId, String tenantId);

	List<QualityInspectionDtlEntity> getQIDtlList(String qiHdrId);

	void resetQiHdrIsLatest(String qiId, String tenantId);

	int insertInQiHdr(QualityInspectionHdrEntity qiHdrDtlReq);

	int updateQIHdr(QualityInspectionHdrEntity qiHdrDtlReq);

	int insertQiDtl(QualityInspectionDtlEntity qualityInspectionDtlEntity);

	int updateQiDtl(QualityInspectionDtlEntity req);

	int insertQiStatus(String refId, String refDoc, String seqNo, String seqStatus, String remarks, String empId,
			String tenantId);

	List<QIStatusEntity> getQIStatusDtls(String refId, String refDoc, String tenantId);

	List<RetrieveQualitInspectionEntity> getPoDtlsByQiHdrId(String qiId);

	List<QualityInspectionHdrEntity> getQiHdrListtByQiHdrId(String qiHdrId, String tenantId);

	List<QiCaEntity> getQiCaList(String pmHdrId, String tenantId, String fromDate, String toDate);

	int insertInQualityCa(String qiHdrId, String poId, String poCode, String poDtlId, String caType, String qty,
			String seqNo, String seqStatus, String isApproved, String pmHdrId, String tenantId);

	List<QiCaEntity> getQiCaListByQiCaId(String qiCaId);

	int resetPoInspectedQty(String receivedQty, String poDtlId);


	int updateQiHdrValues(String caInternal, String caVendor, String reworkInternal, String reworkVendor,
			String rejectedInternal, String rejectedExternal, String qiHdrId, String okQty, String nOkQty, String reworkQty, String empId, String qualityRating);

	String compareQtyAndInsQty(String poDtlId);

	List<QiCaEntity> getQiCaDtlsByQiCaId(String qiCaDtlId, String tenantId);

	String getMiDtlIdByCaDtlId(String qiCaDtlId, String tenantId);

	String getMiIdByMiDtlId(String miDtlId, String tenantId);

	String getInventoryCodeByMiId(String miId);

	String getMiDtlIdByQiHdrId(String qihdrId, String tenantId);

	List<QualityInsReqEntity> getGrnDtls(String qicHdrId, String tenantId);

	List<Integer> getMiCompletedStatus(String miId);

	void updateMiStatus(String miId, String empId);

	int updateQiCaSeqAndStatus(String currentseq, String docStatus, int isApproved, String qiCaDtlId, String empId);

	String getPoCode(String qiCaDtlId);

	int updateInspectedQty(String inspectedQty, String poDtlId, String nokQty, String reWorkQty);

	int updateQiReqQtyInspected(String okquantity, String qiId);

	String getQiIdByQiHdrId(String qiHdrId);

	String getLastSeq(String docType, String tenantId);

	int updateQISeqAndStatus(String currentseq, String docStatus, int isCompleted, String qiHdrId, String empId,
			String nrFLag);

	int updateQiCaDtlValues(String caQty, String reworkInternal, String reworkVendor, String rejectedInternal,
			String rejectedExternal, String qiCaDtlId, String caVendor, String caInternal, String remarks);

	void updateMiQty(String miDtlID, String okQty, String nokQty, String reworkQty);

	String getEmpDepCode(String empId, String tenantId);

	String getPoDelDate(String qHdrId, String tenantId);

	String getInsDate(String qHdrId, String tenantId);
	
	String getOldRelationshipRating(String qHdrId, String tenantId);

	void updateInwardRating(String qHdrId, String tenantId, String poDeliveryDate,String inspDate);
	
	String getInwardRating(String qHdrId, String tenantId, String poDeliveryDate,String inspDate);

	int updateVendorRating(UpdateVendorRatingRequest updateVendorRatingRequest);
	
	int udpatepreinspectionqty(String projectId, String productCode, String locationCode,
			BigDecimal transQty, String operation, String invTransType, String transRefId, String transferredBy,
			String transDateTime, String tenantId);

	int updateCancelInspQualityStatus(String cancelFlag, String empId, String qualityHdrId, String tenantId);

	String getCanLastSeq(String docType, String tenantId);

	String getCurrSeqForQInspection(String refId, String type);

	String getPoId(String projectId, String refId, String tENANT_ID);

	String getIndentHdrIdByQiHdrId(String qihdrId, String tenantId);

	int getCompletedCountByQiId(String qiId, String excludeQiHdrId, String sequenceNo);

}
