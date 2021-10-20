package pl.blaszak.speech.service;

import io.grpc.stub.StreamObserver;
import pl.blaszak.speech.ItemGeneratorGrpc;
import pl.blaszak.speech.SpeechItemRequest;
import pl.blaszak.speech.SpeechItemResponse;

public class ItemGeneratorService extends ItemGeneratorGrpc.ItemGeneratorImplBase {

    @Override
    public void stickOnItem(SpeechItemRequest request, StreamObserver<SpeechItemResponse> responseObserver) {
        SpeechItemResponse response = SpeechItemResponse.newBuilder().setMessage(request.getContent() + "add some content ").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
