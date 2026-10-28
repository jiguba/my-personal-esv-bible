package dev.jasonguba.esvbible.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import dev.jasonguba.esvbible.models.BibleBook;

@Component
public class ESVCrosswayClient {
    private static final Logger logger = LoggerFactory.getLogger(ESVCrosswayClient.class);

    private static final String BASE_URL = "https://api.esv.org";
    private static final String PASSAGE_PATH = "/v3/passage/text/";

    private final RestClient restClient;
    private final String apiKey;

    public ESVCrosswayClient(RestClient.Builder restClientBuilder, @Value("${esvbible.api.key}") String apiKey) {
        this.restClient = restClientBuilder
            .baseUrl(BASE_URL)
            .build();

        if (apiKey.isBlank()) {
            logger.warn("ESV API key not configured");
        }
        this.apiKey = apiKey;
    }

    public String getChapterText(BibleBook bibleBook, int chapterNumber) {
        if (this.apiKey.isBlank()) {
            throw new FailedPassageTextFetchException("ESV API key not configured");
        }
        
        try {
            String displayName = bibleBook.getDisplayName();
            String reference = String.format("%s %d", displayName, chapterNumber);
   
            return restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path(PASSAGE_PATH)
                    .queryParam("q", reference)
                    .build())
                .header("Authorization", apiKey)
                .retrieve()
                .body(String.class);
        } catch (RestClientException e) {
            throw new FailedPassageTextFetchException("Could not get passage text.", e);
        }
    }
}
