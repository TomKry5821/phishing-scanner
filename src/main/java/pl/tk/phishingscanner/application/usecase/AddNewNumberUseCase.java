package pl.tk.phishingscanner.application.usecase;

import static pl.tk.phishingscanner.application.AddNewNumberResult.of;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.AddNewNumberResult;
import pl.tk.phishingscanner.application.port.PhishingScannerUserService;
import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.domain.model.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddNewNumberUseCase {

  private final PhishingScannerUserService phishingScannerUserService;

  public AddNewNumberResult execute(TextMessage textMessage) {
    PhoneNumber sender = textMessage.sender();

    log.trace("Started adding new number to scanner service: {}", sender);
    Optional<PhoneNumber> newNumber = phishingScannerUserService.addUserByPhoneNumber(sender);

    AddNewNumberResult result = of(newNumber.isPresent());
    log.debug("Finished adding new number to scanner service: {}. Result: {}", sender, result);

    return result;
  }
}
