package dev.jasonguba.esvbible.clients;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import dev.jasonguba.esvbible.models.BibleBook;

public class ESVCrosswayClientTest {
    @Test
    void getChapterTextThrowsWithoutCallingEsvWhenKeyIsBlank() {
        var builder = RestClient.builder();
        var esvClient = new ESVCrosswayClient(builder, "");
        var thrown = assertThrows(FailedPassageTextFetchException.class, () -> esvClient.getChapterText(BibleBook.JOHN, 21));
        assertNull(thrown.getCause());
    }

}
