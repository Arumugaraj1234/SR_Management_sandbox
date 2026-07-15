package com.vmfg.quality.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.project.request.PmHdrIdAndTenantIdRequest;
import com.vmfg.quality.entity.QualityInspectionHdrEntity;
import com.vmfg.quality.request.QiCaDtlsRequest;
import com.vmfg.quality.request.QiStatusRequest;
import com.vmfg.quality.request.UpdateQiCaDtlsRequest;
import com.vmfg.quality.request.UpdateVendorRatingRequest;
import com.vmfg.quality.request.getQIDtlsRequest;
import com.vmfg.scm.request.UpdateSeqAndStatusRequest;

public interface IQualityInspectionService {

	ResponseAsList getInspHdrAndDtl(getQIDtlsRequest getQIDtlsReq);

	ResponseAsList getQIStatusDtls(QiStatusRequest qiStatusReq);

	ResponseAsMessage insertInspHdrAndDtl(QualityInspectionHdrEntity qiHdrDtlReq);

	ResponseAsMessage updateQISeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq);

	ResponseAsMessage updateQiCaSeqAndStatus(UpdateSeqAndStatusRequest updateHdrReq);

	ResponseAsList getQiCaDtlsByPmHdrId(PmHdrIdAndTenantIdRequest request);

	ResponseAsList getQiCaDtlsByQiCaDtlId(QiCaDtlsRequest qiCaDtlsReq);

	ResponseAsMessage updateQiCaDtls(UpdateQiCaDtlsRequest updateQiCaDtlsReq);

	ResponseAsMessage updateVendorRating(UpdateVendorRatingRequest updateVendorRatingRequest);

}
