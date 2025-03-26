package com.example.wordcounter.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcConfig {

    @Value("${grpc.server.port}")
    private int grpcPort;

    @Bean
    public Server grpcServer(
            @Autowired(required = false) CoordinatorGrpcServiceImpl coordinatorService,
            @Autowired(required = false) ProposerGrpcServiceImpl proposerService,
            @Autowired(required = false) AcceptorGrpcServiceImpl acceptorService
    ) throws IOException {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcPort);

        if (coordinatorService != null) {
            serverBuilder.addService(coordinatorService);
        }
        if (proposerService != null) {
            serverBuilder.addService(proposerService);
        }
        if (acceptorService != null) {
            serverBuilder.addService(acceptorService);
        }

        return serverBuilder.build().start();
    }
}