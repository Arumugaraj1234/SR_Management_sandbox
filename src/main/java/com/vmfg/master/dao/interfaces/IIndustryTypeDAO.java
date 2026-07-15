package com.vmfg.master.dao.interfaces;

import java.util.List;

import com.vmfg.master.entity.IndustryTypeEntity;

public interface IIndustryTypeDAO {

	List<IndustryTypeEntity> getIndustryTypeInfo(String tENANT_ID);

}
