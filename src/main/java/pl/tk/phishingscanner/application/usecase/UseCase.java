package pl.tk.phishingscanner.application.usecase;

import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.domain.model.TextMessageType;

public interface UseCase {
  ProcessUseCaseResult execute(TextMessage message);

  boolean isTextMessageTypeProcessed(TextMessageType textMessageType);

  boolean acceptsPhoneNumber(PhoneNumber phoneNumber);
}
