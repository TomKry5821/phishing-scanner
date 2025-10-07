package pl.tk.phishingscanner.infra.integration;

import static java.lang.Boolean.FALSE;
import static pl.tk.phishingscanner.infra.integration.model.ConfidenceLevel.SAFE;
import static pl.tk.phishingscanner.infra.integration.model.ThreatType.SOCIAL_ENGINEERING;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.UriVerifier;
import pl.tk.phishingscanner.infra.integration.model.response.VerifyUriResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class UriVerifierImpl implements UriVerifier {

  private final UriVerifierConfig config;

  private final UriVerifierClient client;

  @Override
  public boolean containsPhishing(URI uri) {
    boolean isPhishing =
        client
            .retrieveResponse(uri.toString())
            .map(this::containsNotSafeConfidenceLevel)
            .orElse(config.isPassUnverifiedMessages());
    log.debug("Successfully verified URI: {}. is phishing: {}", uri, isPhishing);
    return isPhishing;
  }

  private boolean containsNotSafeConfidenceLevel(VerifyUriResponse response) {
    log.trace("Processing retrieved verify URI response: {}", response);
    return response.scores().stream()
        .filter(r -> r.threatType() == SOCIAL_ENGINEERING)
        .findFirst()
        .map(s -> s.confidenceLevel() != SAFE)
        .orElse(FALSE);
  }
}
