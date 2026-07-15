package com.vmfg.master.services.interfaces;

import java.util.List;

import com.vmfg.master.entity.IndustryTypeEntity;
import com.vmfg.master.request.IndustryTypeRequest;

public interface IIndustryTypeService {

	List<IndustryTypeEntity> getIndustryTypeInfo(IndustryTypeRequest scop);

}
