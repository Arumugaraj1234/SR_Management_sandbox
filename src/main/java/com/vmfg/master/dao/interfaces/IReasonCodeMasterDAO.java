package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.ReasonCodeMasterEntity;

public interface IReasonCodeMasterDAO {

	List<ReasonCodeMasterEntity> getReasonCodeInfo(String tENANT_ID, String eMPLOYEE_ID);

}
