package com.vmfg.sales.dao.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vmfg.general.entity.DocumentStatusMstEntity;
import com.vmfg.general.entity.DocumentTypeMstEntity;
import com.vmfg.general.entity.GetstageprocessDtlEntity;
import com.vmfg.sales.entity.BudgetExcessUploadResponse;
import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.FinancialYearTransactionMstEntity;
import com.vmfg.sales.entity.SalesBudgetFullEntity;
import com.vmfg.sales.request.EnqEnablementRequest;
import com.vmfg.sales.request.SalesBudgetSheetExntDtlEntity;

public interface IEnquiryDAO {

	int updatecontactDtl(String contactName, String ContactNo, String ContactEmail, String slaveId, String primary, String department);

	int insertcontactDtl(String contactName, String ContactNo, String ContactEmail, String slaveId, String masterId,
			String primary, String department);

	int updateEnqHdr(String projectName, String customerName, String industrialType, String scopeofWork,
			String projectDescription, String productDtl, String enquiryType, String enqCustomerSts, String enqDate,
			String reason, String leadDtl, String tentativePoValue, String tenantId, String mstId, String location , String poDate);

	int insertStgDtl(String masterId, String status, String seq, String tenantId, String tableName);

	List<DocumentTypeMstEntity> getStgDocDtl(String pmId, String tenantId);

	List<DocumentStatusMstEntity> getfirstSeqBypmIdDocType(String reference, String processCode, String tenantId);

	List<FinancialYearTransactionMstEntity> getfinacicalTransDtlByDocType(String pmId, String tenantId);

	List<GetstageprocessDtlEntity> getFirstStageByPmId(String proccessCode, String tenantId);

	List<DocumentTypeMstEntity> getEnqDocTypeMstDtlByStage(String stgCode, String pmId, String tenantId);

	String getEnqProcessLifeCycleCurrSeq(String processCode, String status, String tenantId);

	int insertProcessAssignDtl(String masterId, String empId, String tenantId, String pmId);

	int getCRLastNo();

	int insertEnqHdr(String projectName, String customerName, String industrialType, String scopeofWork,
			String projectDescription, String productDtl, String enquiryType, String enqCustomerSts, String enqDate,
			String reason, String leadDtl, String tentativePoValue, String tenantId, String stageCode, String stageSeq,
			String statusCode, String statusSeq, String location, String poDate,String isInternal);
	
	String setDefaultUser(String pmId,String tenantId);

	List<BudgetExcessUploadResponse> uploadBudgetSheetfile(String tenantId, MultipartFile file);
	
	String getprojectCodeDtl(String enqId,String tenantId);
	
	List<SalesBudgetFullEntity> getsalesHdrDtl(String masterId,String tenantId);
	
	List<SalesBudgetSheetExntDtlEntity>getsaleExtDtlList(String sbExtId,String tenantId);
	
	int updateSaleBudgethdr(String sbHdrId,String paymentTerm,String salePercent,String finalSaleVal,String tenantId);

	int getEnqEnablement(EnqEnablementRequest enqEnablementRequest,String deptCode);
	
	String getsaleEnquiryCode(String seId);

	List<CustomerMstEntity> getCustomerMst(String tenantid);

	String getCusCodeByCusName(String customerName, String tenantId);

	int deleteSaleEnqContact(String hdrId);

	int insertCustomerDtl(String customerName, String city, String state, String country, String pincode,
			String address, String tenantId, String contactNo, String pan, String gst);

	String getsaleEnquiryCurSeq(String masterId);

	String getEmpNameDesingCode(String approveDesig, String tenantId);

}