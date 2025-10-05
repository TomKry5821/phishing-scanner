package pl.tk.phishingscanner.application.usecase;

import java.net.URI;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.enums.ProcessPhishingResult;
import pl.tk.phishingscanner.application.port.PhishingTextMessageSender;
import pl.tk.phishingscanner.application.port.SafeTextMessageSender;
import pl.tk.phishingscanner.application.port.UriVerifier;
import pl.tk.phishingscanner.domain.logic.UriExtractor;
import pl.tk.phishingscanner.domain.model.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessTextMessageUseCase {

  private final UriExtractor uriExtractor;
  private final UriVerifier uriVerifier;
  private final SafeTextMessageSender safeTextMessageSender;
  private final PhishingTextMessageSender phishingTextMessageSender;

  public ProcessPhishingResult execute(TextMessage message) {
    log.trace("Scanning text message: {} for phishing", message);
    ProcessPhishingResult result = scanMessage(message.message());

    switch (result) {
      case PHISHING_DETECTED -> phishingTextMessageSender.send(message);
      case NOT_PHISHING_DETECTED -> safeTextMessageSender.send(message);
    }

    return result;
  }

  private ProcessPhishingResult scanMessage(String message) {
    Set<URI> uris = uriExtractor.extractFromMessage(message);
    boolean phishingDetected = uris.stream().anyMatch(uriVerifier::containsPhishing);
    ProcessPhishingResult result = ProcessPhishingResult.of(phishingDetected);

    log.debug("Scanning message: {} completed. Result: {}", message, result);
    return result;
  }
}
