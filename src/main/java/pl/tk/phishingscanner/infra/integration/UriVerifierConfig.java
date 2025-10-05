package pl.tk.phishingscanner.infra.integration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Configuration
@ConfigurationProperties(prefix = "uri-verifier")
public class UriVerifierConfig {

  private static final String APPLICATION_JSON = "application/json";

  /** Bazowy URL usługi potrzebny do integracji z usługą weryfikacji phishingu. */
  private String baseUrl;

  /** Adres URI usług weryfikacji phishingu. */
  private String evaluateUri;

  /** Token autyoryzacyjny umożliwiający korzystanie z usługi weryfikacji phishingu. */
  private String accessToken;

  /** Ilość prób ponownego zapytania do serwisu w przypadku problemu z otrzmyaniem odpowiedzi. */
  private int retriesNumber = 3;

  /** Ilość sekund pomiędzy kolejnymi próbami ponownych zapytań. */
  private int durationBetweenRetriesInSeconds = 2;

  /**
   * Czy przekazywać wiadomości, których zawartość nie mogła zostać zweryfikowana pod kątem
   * phishingu np. z powodu problemów z usługą.
   */
  private boolean passUnverifiedMessages;

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder
        .baseUrl(baseUrl)
        .defaultHeader(AUTHORIZATION, accessToken)
        .defaultHeader(CONTENT_TYPE, APPLICATION_JSON)
        .build();
  }
}
