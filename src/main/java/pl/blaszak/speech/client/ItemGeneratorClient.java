package pl.blaszak.speech.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.blaszak.speech.ItemGeneratorGrpc;
import pl.blaszak.speech.SpeechItemRequest;

import javax.annotation.PreDestroy;

import static pl.blaszak.speech.GeneratorConfig.AWAIT_TERMINATION_TIME;
import static pl.blaszak.speech.GeneratorConfig.AWAIT_TERMINATION_TIME_UNIT;

public class ItemGeneratorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorClient.class);

    private final ManagedChannel channel;
    private final ItemGeneratorGrpc.ItemGeneratorBlockingStub blockingStub;

    public ItemGeneratorClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = ItemGeneratorGrpc.newBlockingStub(channel);
    }

    public static ManagedChannel forChannelOn(int port) {
        String target = "localhost:" + port;
        return ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(AWAIT_TERMINATION_TIME, AWAIT_TERMINATION_TIME_UNIT);
    }

    public String getItem() {
        SpeechItemRequest request = SpeechItemRequest.newBuilder().build();
        try {
            return blockingStub.getItem(request).getMessage();
        } catch (StatusRuntimeException e) {
            LOGGER.warn("RPC failed: {}", e.getStatus());
            return null;
        }
    }
}
