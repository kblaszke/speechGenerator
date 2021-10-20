package pl.blaszak.speech;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ItemGeneratorServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorServer.class);

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new ItemGeneratorService())
                .build().start();
        LOGGER.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    ItemGeneratorServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        ItemGeneratorServer server = new ItemGeneratorServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class ItemGeneratorService extends ItemGeneratorGrpc.ItemGeneratorImplBase {

        @Override
        public void stickOnItem(SpeechItemRequest request, StreamObserver<SpeechItemResponse> responseObserver) {
            SpeechItemResponse response = SpeechItemResponse.newBuilder().setMessage(request.getContent() + "add some content ").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
