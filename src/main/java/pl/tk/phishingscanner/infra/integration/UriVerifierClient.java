package pl.tk.phishingscanner.infra.integration;

import static java.time.Duration.ofSeconds;
import static reactor.util.retry.Retry.fixedDelay;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.tk.phishingscanner.infra.integration.model.request.VerifyUriRequest;
import pl.tk.phishingscanner.infra.integration.model.response.VerifyUriResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UriVerifierClient {

  private final WebClient webClient;

  private final UriVerifierConfig config;

  public Optional<VerifyUriResponse> retrieveResponse(String uri) {
    log.trace("Retrieving verify uri response for uri: {}", uri);
    var response =
        webClient
            .post()
            .uri(config.getEvaluateUri())
            .bodyValue(new VerifyUriRequest(uri))
            .retrieve()
            .bodyToMono(VerifyUriResponse.class)
            .retryWhen(
                fixedDelay(
                        config.getRetriesNumber(),
                        ofSeconds(config.getDurationBetweenRetriesInSeconds()))
                    .filter(ex -> ex instanceof WebClientResponseException.InternalServerError))
            .doOnError(
                e ->
                    log.warn(
                        "There was problem while retrieving verify for URI: {} response: {}, {}",
                        uri,
                        e.getClass(),
                        e.getMessage()))
            .onErrorResume(t -> Mono.empty())
            .blockOptional();
    log.debug("Successfully retrieved response for URI: {} verification: {}", uri, response);
    return response;
  }
}
