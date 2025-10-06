package pl.tk.phishingscanner.infra.amqp.in;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.ErrorMessage;
import pl.tk.phishingscanner.application.usecase.strategy.UseCaseStrategy;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.infra.amqp.in.event.NewTextMessageEvent;
import pl.tk.phishingscanner.infra.mapping.MessageEventMapper;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TextMessageEventConsumer {

  private final UseCaseStrategy strategy;

  @Bean
  public Consumer<NewTextMessageEvent> textMessageConsumer() {
    return event ->
        MessageEventMapper.map(event)
            .ifPresentOrElse(
                this::handle, () -> log.warn("Message could not be scanned: {}", event));
  }

  @Bean
  public Consumer<ErrorMessage> textMessageErrorConsumer() {
    return errorMessage ->
        log.warn(
            "There was a problem while processing event: {} with cause: {}",
            errorMessage.getOriginalMessage(),
            errorMessage.getPayload().getLocalizedMessage());
  }

  private void handle(TextMessage message) {
    strategy.getUseCase(message).execute(message);
  }
}
