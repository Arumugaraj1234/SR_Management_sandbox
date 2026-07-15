package com.vmfg.mis.services.interfaces;

import com.vmfg.general.response.ResponseAsList;
import com.vmfg.mis.request.DelayedIndentRequest;
import com.vmfg.mis.request.DrillDownRequest;

public interface IDrillDownService {

	ResponseAsList getIndentByNotAvailablePO(DrillDownRequest drillDownRequest);

	ResponseAsList getPoInitalValue(DelayedIndentRequest getPoInitalValueRequest);

	ResponseAsList getInventoryValueDrill(DelayedIndentRequest getPoInitalValueRequest);

	ResponseAsList getDelayedIndent(DrillDownRequest delayedIndentRequest);

	ResponseAsList getNumberOfPoDrill(DelayedIndentRequest getgetInventoryValueRequest);

	ResponseAsList getIndentByInventoryStock(DrillDownRequest drillDownRequest);

}
