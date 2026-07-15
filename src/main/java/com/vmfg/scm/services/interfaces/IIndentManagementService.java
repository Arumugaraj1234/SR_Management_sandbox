package com.vmfg.scm.services.interfaces;

import com.vmfg.design.request.IndentDtlRequest;
import com.vmfg.general.response.ResponseAsList;
import com.vmfg.scm.request.IndentGrpRetRequest;
import com.vmfg.scm.request.ProjectAssignEmpReq;
import com.vmfg.scm.request.ProjectDtlRequest;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;
import com.vmfg.scm.request.getIndentHdrRequest;

public interface IIndentManagementService {

	ResponseAsList getIndentProjectDtlsByDate(ProjectDtlRequest projectDtlReq);

	ResponseAsList indentHdrDropDownByProjectCode(getIndentHdrRequest projectIdRequest);

	ResponseAsList getIndentHdrDtlsByIndentId(IndentDtlRequest indentDtlReq);

	ResponseAsList getScmHdrBasedDtl(ScmHdrBasedDtlRequest scmHdrBasedDtl);

	ResponseAsList getIndentGrpNewProd(IndentGrpRetRequest indentGrpReq);

	ResponseAsList getIndentProjectDtlsByDateAndIndent(ProjectDtlRequest projectDtlReq);

	ResponseAsList getIndentProjectDtlsByEmployee(ProjectAssignEmpReq projectDtlReq);

}
