package pl.blaszak.speech.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.blaszak.speech.ItemGeneratorUtil;
import pl.blaszak.speech.service.ItemGeneratorService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ItemGeneratorServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorServer.class);

    private final Server server;

    public static Server forGRPC(int port, ItemGeneratorService service) {
        return ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    public static ItemGeneratorServer build(Server server) {
        return new ItemGeneratorServer(server);
    }

    private ItemGeneratorServer(Server server) {
        this.server = server;
    }

    public void start() throws IOException {
        server.start();
        LOGGER.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
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

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 50051;
        ItemGeneratorUtil util = new ItemGeneratorUtil();
        ItemGeneratorService service = new ItemGeneratorService(util.loadFile("fragments1.txt"));
        ItemGeneratorServer itemGeneratorServer = ItemGeneratorServer.build(forGRPC(port, service));
        itemGeneratorServer.start();
        itemGeneratorServer.blockUntilShutdown();
    }
}
