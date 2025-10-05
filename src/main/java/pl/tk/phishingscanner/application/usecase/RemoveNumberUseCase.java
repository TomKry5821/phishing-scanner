package pl.tk.phishingscanner.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingScannerUserService;
import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.domain.model.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveNumberUseCase {

  private final PhishingScannerUserService phishingScannerUserService;

  public void execute(TextMessage textMessage) {
    PhoneNumber sender = textMessage.sender();
    log.trace("Started removing number from scanner service: {}", sender);
    phishingScannerUserService.removeUserByPhoneNumber(sender);
    log.debug("Finished removing number from scanner service: {}", sender);
  }
}
