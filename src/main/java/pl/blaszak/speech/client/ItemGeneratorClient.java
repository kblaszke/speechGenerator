package pl.blaszak.speech.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.blaszak.speech.ItemGeneratorGrpc;
import pl.blaszak.speech.SpeechItemRequest;
import pl.blaszak.speech.SpeechItemResponse;

import java.util.concurrent.TimeUnit;

public class ItemGeneratorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorClient.class);

    private final ItemGeneratorGrpc.ItemGeneratorBlockingStub blockingStub;

    public ItemGeneratorClient(Channel channel) {
        this.blockingStub = ItemGeneratorGrpc.newBlockingStub(channel);
    }

    public void stickOnItem(String speechItem) {
        LOGGER.info("Will try to make speech item from: " + speechItem + " ...");
        SpeechItemRequest request = SpeechItemRequest.newBuilder().setContent(speechItem).build();
        SpeechItemResponse response;
        try {
            response = blockingStub.stickOnItem(request);
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {}", e.getStatus());
            return;
        }
        LOGGER.info("Got {} ", response.getMessage());
    }

    public static void main(String[] args) throws Exception {
        String target = "localhost:50051";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        try {
            ItemGeneratorClient client = new ItemGeneratorClient(channel);
            client.stickOnItem("");
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
