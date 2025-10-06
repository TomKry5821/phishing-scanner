package pl.tk.phishingscanner.application.usecase.strategy;

import static pl.tk.phishingscanner.domain.model.TextMessageType.SCAN_MESSAGE;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.usecase.UseCase;
import pl.tk.phishingscanner.domain.model.TextMessageType;

@Component
@RequiredArgsConstructor
public class UseCaseStrategy {

  private final Set<UseCase> useCases;

  public UseCase getUseCase(TextMessageType messageType) {
    return useCases.stream()
        .filter(uc -> uc.isTextMessageTypeProcessed(messageType))
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
