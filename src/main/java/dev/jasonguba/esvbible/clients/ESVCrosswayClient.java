package dev.jasonguba.esvbible.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ESVCrosswayClient {
    private static final String BASE_URL = "https://api.esv.org"; 
    private static final String PASSAGE_PATH = "/v3/passage/text/";

    private final RestClient restClient;
    private final String apiKey;

    public ESVCrosswayClient(RestClient.Builder restClientBuilder, @Value("${esvbible.api.key}") String apiKey) {
        this.restClient = restClientBuilder
            .baseUrl(BASE_URL)
            .build();

        this.apiKey = apiKey;
    }

    public String getPassageText() {
        return restClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(PASSAGE_PATH)
                .queryParam("q","John+3:16")
                .build())
            .header("Authorization", apiKey)
            .retrieve()
            .body(String.class);
    }
}
