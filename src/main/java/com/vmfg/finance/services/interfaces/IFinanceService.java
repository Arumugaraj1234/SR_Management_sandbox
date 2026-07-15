package com.vmfg.finance.services.interfaces;

import com.vmfg.design.request.DesignRequest;
import com.vmfg.general.response.ResponseAsList;

public interface IFinanceService {

	ResponseAsList getFinanceDtl(DesignRequest designReq);

}
