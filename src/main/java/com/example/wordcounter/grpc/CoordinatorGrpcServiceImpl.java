package com.example.wordcounter.grpc;

import com.example.wordcounter.dto.ClusterState;
import com.example.wordcounter.dto.NodeType;
import com.example.wordcounter.service.CoordinatorService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@GrpcService
@Profile("coordinator")
public class CoordinatorGrpcServiceImpl extends WordCounterServiceGrpc.WordCounterServiceImplBase {

    @Autowired
    private CoordinatorService coordinatorService;

    @Override
    public void registerNode(NodeRegistrationRequest request,
                             StreamObserver<NodeRegistrationResponse> responseObserver) {
        NodeRegistration registration = new NodeRegistration(
                NodeType.valueOf(request.getNodeType().toUpperCase()),
                request.getAddress()
        );

        coordinatorService.registerNode(registration);

        NodeRegistrationResponse response = NodeRegistrationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Registered successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

@GrpcService
@Profile("proposer")
class ProposerGrpcServiceImpl extends WordCounterServiceGrpc.WordCounterServiceImplBase {

    @Autowired
    private ProposerController proposerController;

    @Override
    public void processLine(ProcessLineRequest request,
                            StreamObserver<ProcessLineResponse> responseObserver) {
        proposerController.processLine(request.getLine());

        ProcessLineResponse response = ProcessLineResponse.newBuilder()
                .setAcknowledged(true)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

@GrpcService
@Profile("acceptor")
class AcceptorGrpcServiceImpl extends WordCounterServiceGrpc.WordCounterServiceImplBase {

    @Autowired
    private AcceptorController acceptorController;

    @Override
    public void submitResults(ResultsSubmissionRequest request,
                              StreamObserver<ResultsSubmissionResponse> responseObserver) {
        Map<Character, WordCount> counts = convertProtoMap(request.getCountsMap());

        boolean isValid = acceptorController.validate(counts);

        ResultsSubmissionResponse response = ResultsSubmissionResponse.newBuilder()
                .setAccepted(isValid)
                .setValidationMessage(isValid ? "Valid results" : "Invalid results")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Map<Character, WordCount> convertProtoMap(Map<String, WordCount> protoMap) {
        // Conversion logic from protobuf map to your domain model
    }
}