package com.vmfg.scm.dao.interfaces;

import java.util.List;

import com.vmfg.scm.entity.DebitNoteDtlListEntity;
import com.vmfg.scm.entity.DebitNoteStatusEntity;
import com.vmfg.scm.entity.GetDebitNoteEntity;
import com.vmfg.scm.request.DebitNoteDtlRequest;
import com.vmfg.scm.request.DebitNoteHdrAndDtlRequest;

public interface IDebitNoteDAO {

	int insertDebitNoteHdrAndDtl(DebitNoteHdrAndDtlRequest debitNoteRequest, String debitCode);

	int updateIndentQtyByPoDtlQty(DebitNoteDtlRequest debitNoteDtlRequest, String poDtlQty);

	int insertDebitNoteReason(String dnReason, String tenantId);

	int updateDebitNoteHdr(String seq, String seqStatus, String isLast, String currentDateTime, String empId,
			String dnId);

	int insertDebitNoteStatusDtl(String dnId, String seq, String seqStatus, String tenantId, String remarks,
			String empId);

	List<GetDebitNoteEntity> getDebitNoteHdrListByPmHdrId(String pmHdrId, String poId, String tenantId);

	List<DebitNoteDtlListEntity> getDebitNoteSubList(String dnId);

	int getDmIdByLatestVerionForDebit(String dmId, String docType, String tenantId);

	String getLastDebitCode();

	List<DebitNoteStatusEntity> getDebitNoteStatusList(String dnId);


}
