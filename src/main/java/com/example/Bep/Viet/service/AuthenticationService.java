package com.example.Bep.Viet.service;

import com.example.Bep.Viet.request.AuthenticationRequest;
import com.example.Bep.Viet.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
