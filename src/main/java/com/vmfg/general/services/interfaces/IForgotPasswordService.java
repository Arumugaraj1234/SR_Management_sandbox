package com.vmfg.general.services.interfaces;

import com.vmfg.general.request.ResetPasswordRequest;
import com.vmfg.general.request.getOtpRequest;
import com.vmfg.general.response.ResponseAsMessage;

public interface IForgotPasswordService {

	ResponseAsMessage generateOtp(getOtpRequest getOtpReq);

	ResponseAsMessage verifyOtp(getOtpRequest getOtpReq);

	ResponseAsMessage resetPassword(ResetPasswordRequest ResetPasswordReq);

}
