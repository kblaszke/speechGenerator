package pl.blaszak.speech;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;

import static pl.blaszak.speech.GeneratorConfig.AWAIT_TERMINATION_TIME;
import static pl.blaszak.speech.GeneratorConfig.AWAIT_TERMINATION_TIME_UNIT;

public final class ItemGeneratorServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGeneratorServer.class);

    private final Server server;

    private ItemGeneratorServer(Server server) {
        this.server = server;
    }

    public static Server forGRPC(int port, ItemGeneratorService service) {
        return ServerBuilder.forPort(port)
                .addService(service)
                .build();
    }

    public static ItemGeneratorServer build(Server server) {
        ItemGeneratorServer itemGeneratorServer = new ItemGeneratorServer(server);
        itemGeneratorServer.start();
        return itemGeneratorServer;
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(AWAIT_TERMINATION_TIME, AWAIT_TERMINATION_TIME_UNIT);
        }
    }

    public void start() {
        try {
            server.start();
        } catch (IOException e) {
            LOGGER.error("Can not start ItemGeneratorServer ", e);
        }
        LOGGER.info("Server started, listening on " + server.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.debug("*** shutting down gRPC server since JVM is shutting down");
            try {
                ItemGeneratorServer.this.stop();
            } catch (InterruptedException e) {
                LOGGER.error("Can not stop ItemGeneratorServer ", e);
            }
            LOGGER.debug("*** server shut down");
        }));
    }
}
