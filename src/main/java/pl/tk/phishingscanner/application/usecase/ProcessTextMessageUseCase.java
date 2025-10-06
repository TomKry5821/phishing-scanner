package pl.tk.phishingscanner.application.usecase;

import static pl.tk.phishingscanner.application.usecase.ProcessUseCaseResult.SUCCESS;
import static pl.tk.phishingscanner.application.usecase.ProcessUseCaseResult.of;
import static pl.tk.phishingscanner.domain.model.TextMessageType.SCAN_MESSAGE;

import java.net.URI;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingScannerUserService;
import pl.tk.phishingscanner.application.port.PhishingTextMessageSender;
import pl.tk.phishingscanner.application.port.SafeTextMessageSender;
import pl.tk.phishingscanner.application.port.UriVerifier;
import pl.tk.phishingscanner.domain.logic.UriExtractor;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.domain.model.TextMessageType;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessTextMessageUseCase implements UseCase {

  private final UriExtractor uriExtractor;
  private final UriVerifier uriVerifier;
  private final SafeTextMessageSender safeTextMessageSender;
  private final PhishingTextMessageSender phishingTextMessageSender;
  private final PhishingScannerUserService phishingScannerUserService;

  @Override
  public ProcessUseCaseResult execute(TextMessage message) {
    log.trace("Scanning text content: {} for phishing", message);
    ProcessUseCaseResult result = scanMessageIfNeeded(message);

    switch (result) {
      case FAILURE -> phishingTextMessageSender.send(message);
      case SUCCESS -> safeTextMessageSender.send(message);
    }
    return result;
  }

  @Override
  public boolean isTextMessageTypeProcessed(TextMessageType textMessageType) {
    return SCAN_MESSAGE == textMessageType;
  }

  private ProcessUseCaseResult scanMessageIfNeeded(TextMessage message) {
    if (!phishingScannerUserService.isUser(message.receiver())) {
      log.debug("Phone number {} is not user. Omitting scan", message.receiver());
      return SUCCESS;
    }
    return scanMessage(message.content());
  }

  private ProcessUseCaseResult scanMessage(String message) {
    Set<URI> uris = uriExtractor.extractFromMessage(message);
    boolean isSafe = uris.stream().noneMatch(uriVerifier::containsPhishing);
    ProcessUseCaseResult result = of(isSafe);

    log.debug("Scanning content: {} completed. Result: {}", message, result);
    return result;
  }
}
