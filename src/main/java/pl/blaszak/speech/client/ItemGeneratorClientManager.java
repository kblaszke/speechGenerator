package pl.blaszak.speech.client;

import java.util.List;
import java.util.stream.Collectors;

public class ItemGeneratorClientManager {

    private final List<ItemGeneratorClient> clients;

    public ItemGeneratorClientManager(List<ItemGeneratorClient> clients) {
        this.clients = clients;
    }

    public String createSentence() {
        return clients.stream()
                .map(ItemGeneratorClient::getItem)
                .collect(Collectors.joining(" "));
    }
}
