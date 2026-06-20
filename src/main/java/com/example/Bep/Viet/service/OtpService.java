package com.example.Bep.Viet.service;

import com.example.Bep.Viet.enums.OtpType;

public interface OtpService {
    void sendOtp(String email, OtpType type);
    boolean verifyOtp(String email, String otp, OtpType type);
}
