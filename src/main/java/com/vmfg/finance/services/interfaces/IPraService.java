package com.vmfg.finance.services.interfaces;

import com.vmfg.finance.request.*;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;

public interface IPraService {

	ResponseAsMessage insertPRA(PraInsertRequest praInsertRequest);

	ResponseAsList getPraDtl(getPraDtlRequest getPraDtlReq);

	ResponseAsMessage praCancel(PraCancelRequest praCancelRequest);

	ResponseAsList retrievePRA(RetrievePraRequest retrievePraReq);
	
	ResponseAsMessage udpatePraHdrSeq(UdpatePraHdrSeqRequest udpatePraHdrSeqReq);
}
