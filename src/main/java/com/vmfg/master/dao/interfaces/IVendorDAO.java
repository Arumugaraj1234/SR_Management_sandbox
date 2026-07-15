package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.CustomerMstEntity;
import com.vmfg.master.entity.VendorCategoryEntity;
import com.vmfg.master.entity.VendorMstEntity;
import com.vmfg.master.entity.VendorRatingEntity;

public interface IVendorDAO {

	List<VendorMstEntity> getApprVendorDtls(String approved, String tenantId);
	
	int updateVendorDtls(String vendorCode, String vendorName, String gst, String pan, String arn, String email,
			String vendorStatus, String poType, String tenantId, String locationId,String contactNo, String vendorType, String vendorCategory, String gstType, String currencyType);

	List<VendorMstEntity> getAllVendorDtls(String vendorCode, String tenantId);

	int insertVendorDtls(String locReferName, String locAddLine, String city, String state, String countryCode,
			String pinCode, String poType, String tenantId, int locId,String contactNo, String vendorType, String vendorCategory, String gstType, String currencyType);

	int insertLocDtls(String tenantId, String locReferName, String locAddLine, String city, String state,
			String countryCode, String pinCode);
	
	int checkVendorname(String vendorName,String tenantId);

	int updateLocDtls(String locReferName, String locAddLine, String city, String state, String countryCode,
			String pinCode, String locationId,String tenantId);

	List<CustomerMstEntity> getAllCustomerDtl(String tenantID);

	String checkSupplyCatCodeExist(String tenantid, String categoryDesc);

	String insertVendorCategory(String vendor, String tenantId);

	List<VendorCategoryEntity> getVendorCategory(String tenantID);

	List<VendorRatingEntity> getVendorInspRatingDtls(String vendorCode);

	int updateInspectionRaised(String vendorCode);
	
	int updateQtyInspectionRating(String qiHdrId,String tenantId,String qtyRating,String inwardRating,String relationshipRating,String customerComplain);

	String getDmId(String refId, String docType, String uploadDocType);

	int getInspectionRaisedVal(String vendorCode, String tenantId);

	int checkInspectionDate(String vendorCode, String tenantId);

	int vendorDtlInsert(String vendorCode, String inspDate, String inspRating, String empId);

	int updateInspectionDate(String vendorCode, String inspDate);

	String getNextInspDate(String vendorCode, String tenantId);

	int getInspRaisedBtn(String vendorCode, String tenantId);

	int getVendorCurrSeq(String vendorCode);

	int updateVendorIsLatest(String vendorCode);

	String getLatestVenDtlId(String vendorCode);

	List<VendorMstEntity> getVendorRatingDtls(String approved, String tenantId);

	String getvDtlId();

	int updateCusMasDtl(CustomerMstEntity updateCus);

}
