package pl.blaszak.speech;

import io.grpc.stub.StreamObserver;

import java.util.Random;

public class ItemGeneratorService extends ItemGeneratorGrpc.ItemGeneratorImplBase {

    private final String[] fragmentTable;
    private final Random random;

    public ItemGeneratorService(String[] fragmentTable) {
        this.fragmentTable = fragmentTable;
        this.random = new Random();
    }

    @Override
    public void getItem(SpeechItemRequest request, StreamObserver<SpeechItemResponse> responseObserver) {
        SpeechItemResponse response = SpeechItemResponse.newBuilder()
                .setMessage(drawItem())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private String drawItem() {
        int sentenceNumber = random.nextInt(fragmentTable.length);
        return fragmentTable[sentenceNumber];
    }
}
