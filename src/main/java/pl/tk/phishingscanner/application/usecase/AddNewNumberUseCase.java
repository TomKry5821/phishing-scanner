package pl.tk.phishingscanner.application.usecase;

import static pl.tk.phishingscanner.application.usecase.ProcessUseCaseResult.of;
import static pl.tk.phishingscanner.domain.model.TextMessageType.ADD_NUMBER_FOR_SCAN;

import java.util.Optional;
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
public class AddNewNumberUseCase implements UseCase {

  private final PhishingScannerUserService phishingScannerUserService;

  @Override
  public ProcessUseCaseResult execute(TextMessage textMessage) {
    PhoneNumber sender = textMessage.sender();

    log.trace("Started adding new number to scanner service: {}", sender);
    Optional<PhoneNumber> newNumber = phishingScannerUserService.addUserByPhoneNumber(sender);

    ProcessUseCaseResult result = of(newNumber.isPresent());
    log.debug("Finished adding new number to scanner service: {}. Result: {}", sender, result);

    return result;
  }

  @Override
  public boolean isTextMessageTypeProcessed(TextMessageType textMessageType) {
    return ADD_NUMBER_FOR_SCAN == textMessageType;
  }
}
