package com.vmfg.assembly.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import com.vmfg.assembly.entity.MsHdrRetrieveEntity;
import com.vmfg.assembly.entity.RetrieveForMSEntity;
import com.vmfg.assembly.entity.RetrieveMSDtlByHdrEntity;

public interface IAssemblyStagingDAO {

	List<MsHdrRetrieveEntity> msHdrRetrieve(String hdrId, String tenantId);

	List<MsHdrRetrieveEntity> msHdrRetrieveAll(String hdrId, String tenantId);

	List<RetrieveMSDtlByHdrEntity> retrieveMSDtlByHdr(String hdrId, String tenantId);

	int cancelMsHdrReq(String hdrId, String tenantId, String empId, String qty, String projectId, String productId);

	int useMsHdrForReturn(String hdrId, String tenantId, String empId, String qty, String projectId,
			String productId);

	List<RetrieveForMSEntity> retrieveForMS(String hdrId, String tenantId);

	int insertMsHdr(String pmHdrId, String msName, String stageQty, String tenantId, String createdBy, String uom);

	int insertMsDtl(int responseMsHdrId, String productId, String qty, String tenantId, String pmHdrId,
			String createdBy, String msName);

	BigDecimal checkInv(List<MsHdrRetrieveEntity> list, String tenantId);

	void updateMsDtlInv(String productId, String qty, String projectId, String tenantId, String empId, String dtlId,
			String msHdrId, String msName);

	String prodIdFromDesc(String msName, String pmHdrId, String tenantId);

	List<MsHdrRetrieveEntity> msHdrRetrieveByHdrId(String hdrId, String tenantId);

	int checkMsHdrNameExists(String pmHdrId, String msName, String tenantId);

}
