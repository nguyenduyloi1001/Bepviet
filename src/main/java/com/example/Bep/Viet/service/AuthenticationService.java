package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.AuthenticationRequest;
import com.example.Bep.Viet.request.OtpVerifyRequest;
import com.example.Bep.Viet.request.ResetPasswordRequest;
import com.example.Bep.Viet.request.UserRequest;
import com.example.Bep.Viet.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    // Register
    void initiateRegister(UserRequest request);
    void completeRegister(OtpVerifyRequest request);

    // Forgot password
    void initiateForgotPassword(String email);
    void verifyForgotPasswordOtp(OtpVerifyRequest request);
    void resetPassword(ResetPasswordRequest request);
}
