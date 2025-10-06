package pl.tk.phishingscanner.infra.amqp.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.SafeTextMessageSender;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.infra.mapping.MessageEventMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class SafeTextMessageSenderImpl implements SafeTextMessageSender {

  private static final String SAFE_MESSAGE_BINDING = "safe-messages";

  private final StreamBridge publisher;

  @Override
  public void send(TextMessage message) {
    log.trace("Sending text message to safe messages queue: {}", message);
    publisher.send(SAFE_MESSAGE_BINDING, MessageEventMapper.map(message));
  }
}
