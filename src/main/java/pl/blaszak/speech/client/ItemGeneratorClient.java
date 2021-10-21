package pl.blaszak.speech.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.blaszak.speech.ItemGeneratorGrpc;
import pl.blaszak.speech.SpeechItemRequest;

import java.util.concurrent.TimeUnit;

public class ItemGeneratorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorClient.class);

    private static final long AWAIT_TERMINATION_TIME = 5;
    private static final TimeUnit AWAIT_TERMINATION_TIME_UNIT = TimeUnit.SECONDS;

    private final ManagedChannel channel;
    private final ItemGeneratorGrpc.ItemGeneratorBlockingStub blockingStub;

    public static ManagedChannel forChannelOn(int port) {
        String target = "localhost:" + port;
        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }

    public static ItemGeneratorClient build(ManagedChannel channel) {
        return new ItemGeneratorClient(channel);
    }

    public ItemGeneratorClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = ItemGeneratorGrpc.newBlockingStub(channel);
    }

    public String stickOnItem(String speechItem) {
        LOGGER.info("Will try to make speech item from: " + speechItem + " ...");
        SpeechItemRequest request = SpeechItemRequest.newBuilder().setContent(speechItem).build();
        try {
            return blockingStub.stickOnItem(request).getMessage();
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {}", e.getStatus());
            return null;
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(AWAIT_TERMINATION_TIME, AWAIT_TERMINATION_TIME_UNIT);
    }

    public static void main(String[] args) throws Exception {
        ItemGeneratorClient client = ItemGeneratorClient.build(forChannelOn(50051));
        try {
            for(int i = 0; i < 8; i++) {
                String item = client.stickOnItem("");
                System.out.println("************* " + item);
            }
        } finally {
            client.shutdown();
        }
    }
}
