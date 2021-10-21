package pl.blaszak.speech.service;

import io.grpc.stub.StreamObserver;
import pl.blaszak.speech.ItemGeneratorGrpc;
import pl.blaszak.speech.SpeechItemRequest;
import pl.blaszak.speech.SpeechItemResponse;

import java.util.Random;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class ItemGeneratorService extends ItemGeneratorGrpc.ItemGeneratorImplBase {

    private final String[] fragmentTable;
    private final Random random;

    public ItemGeneratorService(String[] fragmentTable) {
        this.fragmentTable = fragmentTable;
        this.random = new Random();
    }

    @Override
    public void stickOnItem(SpeechItemRequest request, StreamObserver<SpeechItemResponse> responseObserver) {
        String input = request.getContent();
        String stringResponse = !isEmpty(input) ?  input + " " + drawItem() : drawItem();
        SpeechItemResponse response = SpeechItemResponse.newBuilder()
                .setMessage(stringResponse)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String drawItem() {
        int sentenceNumber = random.nextInt(fragmentTable.length);
        return fragmentTable[sentenceNumber];
    }
}
