package pl.blaszak.speech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.blaszak.speech.client.ItemGeneratorClientManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class GeneratorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GeneratorApplication.class, args);
        ItemGeneratorClientManager clientManager = context.getBean(ItemGeneratorClientManager.class);
        int speechLength = 20;
        List<String> generatedSpeech = IntStream.range(0, speechLength).boxed()
                .map(i -> clientManager.createSentence())
                .collect(Collectors.toList());
        for (String line : generatedSpeech) {
            System.out.println(line);
        }
    }
}
