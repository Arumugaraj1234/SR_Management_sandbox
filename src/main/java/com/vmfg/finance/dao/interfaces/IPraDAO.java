package com.vmfg.finance.dao.interfaces;

import java.util.List;

import com.vmfg.finance.entity.*;
import com.vmfg.finance.request.PraCancelRequest;
import com.vmfg.finance.request.PraInsertRequest;

public interface IPraDAO {


	String getLastPraCode();

	int InsertPraHdr(PraInsertRequest praInsertRequest, String praCode, String islast);

	int insertPraStatusDtl(String praId, String seqNo, String seqStatusCode, String tenantId, String remarks,
						  String empId);

	String getPoCostTypeDesc(String praId, String tenantId);

	int InsertPraDtl(String string, int praHdrId);

	String grnDtlList(String grnHdrId,String tenantId);
	
	int updatePraHdr(String invoiceNo ,String invoiceDate, String transportValue, String pfValue, String insuranceValue, String otherValue ,String reamarks,String tds,String amountPayable,String retention, String ld, String others ,String praId);

	int updPraAfterPoCancel(String poId, String newPoId, String tenantId);

	List<GetPraDtlEntity> getPraDtlList(String praId,String tenantId);

	int praCancel(String praId, String tenantId);

	List<String> getPraIdsByPoId(String poId, String tenantId);

	List<PraStatusEntity> getPraStatusList(String praId);
	
	List<PraDtlListEntity> getpraSubList(String praId,String tenantId);
	
	List<GetPraDtlEntity> getPraHdrListByPmHdrId(String pmHdrId,String poId,String tenantId);
	
	int updatePraHdrSeq(String seq,String seqCode,String islast,String lastCurr,String praId);

	int getMstId(String string);

	int getProjId(String ProjectId);

	List<GrnDtlsEntity> getgrnlist(String grnHdrId, String tenantId);

	List<PraDtlsHistoryEntity> getprahistory(String praId, String tenantId);

	String getPotPendingAmnt(String potId);

	int updatePotPendingAmnt(String potId, String pending);

	String getDtlId(String praHdrId, String praDtlId);

	List<String> getIndentDtlId(String praId, String tenantId);
	
	String getCountAsCompleted(String indentDtl, String tenantId);

	String getVerCheckByPraId(String praId, String tenantId);

}
