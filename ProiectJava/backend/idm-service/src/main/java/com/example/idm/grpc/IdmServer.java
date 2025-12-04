package com.example.idm.grpc;

import com.example.idm.db.UserRepository;
import com.example.idm.jwt.JwtUtil;
import com.example.idm.jwt.TokenBlackList;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class IdmServer {

    public static void main(String[] args) throws Exception {

        String jdbcUrl = "jdbc:mariadb://mariadb:3306/eventdb";
        String dbUser = "event_user";
        String dbPass = "event_pass";

        UserRepository userRepository = new UserRepository(jdbcUrl, dbUser, dbPass);
        JwtUtil jwtUtil = new JwtUtil();
        TokenBlackList tokenBlackList = new TokenBlackList();

        Server server = ServerBuilder
                .forPort(9090)
                .addService(new IdmServiceImpl(userRepository, jwtUtil, tokenBlackList))
                .build();

        System.out.println("Starting IDM gRPC server on port 9090...");

        server.start();

        System.out.println("IDM gRPC server started.");

        server.awaitTermination();
    }
}
