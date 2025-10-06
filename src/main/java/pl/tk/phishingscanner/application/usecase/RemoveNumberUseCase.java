package pl.tk.phishingscanner.application.usecase;

import static pl.tk.phishingscanner.application.usecase.ProcessUseCaseResult.SUCCESS;
import static pl.tk.phishingscanner.domain.model.TextMessageType.REMOVE_NUMBER_FROM_SCAN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingScannerUserService;
import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.domain.model.TextMessageType;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveNumberUseCase implements UseCase {

  private final PhishingScannerUserService phishingScannerUserService;

  public ProcessUseCaseResult execute(TextMessage textMessage) {
    PhoneNumber sender = textMessage.sender();
    log.trace("Started removing number from scanner service: {}", sender);
    phishingScannerUserService.removeUserByPhoneNumber(sender);
    log.debug("Finished removing number from scanner service: {}", sender);

    return SUCCESS;
  }

  @Override
  public boolean isTextMessageTypeProcessed(TextMessageType textMessageType) {
    return REMOVE_NUMBER_FROM_SCAN == textMessageType;
  }
}
