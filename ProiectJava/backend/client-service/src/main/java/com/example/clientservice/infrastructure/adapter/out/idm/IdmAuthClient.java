package com.example.clientservice.infrastructure.adapter.out.idm;

import com.example.idm.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class IdmAuthClient {

    private final IdmServiceGrpc.IdmServiceBlockingStub stub;

    public IdmAuthClient(IdmServiceGrpc.IdmServiceBlockingStub stub) {
        this.stub = stub;
    }

    public LoginResponse login(String email, String password) {
        LoginRequest req = LoginRequest.newBuilder()
                .setUsername(email)
                .setPassword(password)
                .build();
        return stub.login(req);
    }

    public ValidateTokenResponse validate(String token) {
        TokenRequest req = TokenRequest.newBuilder()
                .setToken(token)
                .build();
        return stub.validateToken(req);
    }

    public LogoutResponse logout(String token) {
        TokenRequest req = TokenRequest.newBuilder()
                .setToken(token)
                .build();
        return stub.logout(req);
    }
}
