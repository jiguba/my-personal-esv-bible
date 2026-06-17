package dev.jasonguba.esvbible.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ESVCrosswayClient {
    private static final String BASE_URL = "https://api.esv.org"; 
    private static final String PASSAGE_PATH = "/v3/passage/text/";

    private final RestClient restClient;

    public ESVCrosswayClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
            .baseUrl(BASE_URL)
            .build();
    }

    public String getPassageText() {
        return restClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(PASSAGE_PATH)
                .build())
            .retrieve()
            .body(String.class);
    }
}
