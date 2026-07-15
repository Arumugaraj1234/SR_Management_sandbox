package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.mis.request.ManagementProjRequest;

public interface IManagementMisService {

	ResponseAsList getTotalProjCnt(ManagementProjRequest manageProjCnt);

	ResponseAsList getProjConsumedValue(ManagementProjRequest manageProjCnt);

	ResponseAsList getProjSpentDrillDown(ManagementProjRequest manageProjCnt);

	ResponseAsList getOverAllProjSpentDrillDown(ManagementProjRequest manageProjCnt);

	ResponseAsList getProjActualValDrillDown(ManagementProjRequest manageProjCnt);

	ResponseAsList getProjDetailsDrillDown(ManagementProjRequest manageProjCnt);

	ResponseAsList getVendorDetailDrillDown(ManagementProjRequest manageProjCnt);

	ResponseAsList getVendorDetailHdrView(ManagementProjRequest manageProjCnt);

	ResponseAsList getVendorPaymentDetails(ManagementProjRequest manageProjCnt);

	ResponseAsList getProjSpentDetailByPmId(ManagementProjRequest manageProjCnt);

}
