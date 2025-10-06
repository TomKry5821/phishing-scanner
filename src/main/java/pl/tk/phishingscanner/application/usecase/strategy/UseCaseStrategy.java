package pl.tk.phishingscanner.application.usecase.strategy;

import static pl.tk.phishingscanner.domain.model.TextMessageType.SCAN_MESSAGE;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.usecase.UseCase;
import pl.tk.phishingscanner.domain.model.TextMessage;

@Component
@RequiredArgsConstructor
public class UseCaseStrategy {

  private final Set<UseCase> useCases;

  public UseCase getUseCase(TextMessage message) {
    return useCases.stream()
        .filter(uc -> uc.acceptsPhoneNumber(message.receiver()))
        .filter(uc -> uc.isTextMessageTypeProcessed(message.type()))
        .findFirst()
        .orElseGet(this::getGeneralUseCase);
  }

  private UseCase getGeneralUseCase() {
    return useCases.stream()
        .filter(uc -> uc.isTextMessageTypeProcessed(SCAN_MESSAGE))
        .findFirst()
        .orElseThrow();
  }
}
