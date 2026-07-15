package com.vmfg.master.services.interfaces;

import java.util.List;

import com.vmfg.master.entity.ReasonCodeMasterEntity;
import com.vmfg.master.request.ReasonCodeMasterRequest;

public interface IReasonCodeMasterService {

	List<ReasonCodeMasterEntity> getReasonCodeInfo(ReasonCodeMasterRequest scop);

}
