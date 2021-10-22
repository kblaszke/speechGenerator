package pl.blaszak.speech;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.blaszak.speech.client.ItemGeneratorClient;
import pl.blaszak.speech.client.ItemGeneratorClientManager;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.blaszak.speech.client.ItemGeneratorClient.forChannelOn;
import static pl.blaszak.speech.ItemGeneratorServer.forGRPC;

@Configuration
public class GeneratorConfig {

    public static final long AWAIT_TERMINATION_TIME = 10;
    public static final TimeUnit AWAIT_TERMINATION_TIME_UNIT = TimeUnit.SECONDS;

    public static final int[] PORTS = {50051, 50052, 50053, 50054};

    private final ItemGeneratorUtil util = new ItemGeneratorUtil();

    @Bean
    public ItemGeneratorClientManager clientManager() {
        List<ItemGeneratorClient> clients = IntStream.range(0, PORTS.length).boxed()
                .map(i -> new ItemGeneratorClient(forChannelOn(PORTS[i])))
                .collect(Collectors.toList());
        return new ItemGeneratorClientManager(clients);
    }

    @Bean
    public List<ItemGeneratorServer> servers() {
        return IntStream.range(0, PORTS.length).boxed().map(i -> {
            ItemGeneratorService service = new ItemGeneratorService(util.loadFile("fragments" + i + ".txt"));
            return ItemGeneratorServer.build(forGRPC(PORTS[i], service));
        }).collect(Collectors.toList());
    }
}
