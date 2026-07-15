package com.vmfg.scm.services.interfaces;

import com.vmfg.finance.request.RetrievePraRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.scm.request.DebitNoteHdrAndDtlRequest;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface IDebitNoteService {

	ResponseAsMessage insertDebitNoteHdrAndDtl(DebitNoteHdrAndDtlRequest debitNoteRequest);

	ResponseAsMessage updateDebitNoteHdr(DebitNoteHdrAndDtlRequest debitNoteRequest);

	ResponseAsList retrieveDebitNote(RetrievePraRequest retrievePraReq);

	ResponseAsMessage insertDebitNoteFileByDnID(JSONObject jsonObj, MultipartFile file);

}
