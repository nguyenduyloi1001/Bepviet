package com.example.Bep.Viet.service.Imp;

import com.example.Bep.Viet.exception.AppException;
import com.example.Bep.Viet.exception.ErrorCode;
import com.example.Bep.Viet.model.User;
import com.example.Bep.Viet.repository.UserRepository;
import com.example.Bep.Viet.request.AuthenticationRequest;
import com.example.Bep.Viet.response.AuthenticationResponse;
import com.example.Bep.Viet.response.UserResponse;
import com.example.Bep.Viet.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationImpl implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String signerKey;

    @NonFinal
    @Value("${jwt.expiration}")
    long expiration;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        UserResponse userResponse = mapToUserResponse(user);

        // 🔥 2. Đút cục userResponse béo tốt vào trong AuthenticationResponse trả về
        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(true)
                .userResponse(userResponse) // Nạp thông tin user sang Frontend
                .build();
    }
    //helper
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl()) // <-- Gán link ảnh từ Cloudinary ở DB ra đây!
                .bio(user.getBio())
                .location(user.getLocation())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("BepViet.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .build();

        JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Không thể tạo token", e);
            throw new RuntimeException(e);
        }
    }
}
