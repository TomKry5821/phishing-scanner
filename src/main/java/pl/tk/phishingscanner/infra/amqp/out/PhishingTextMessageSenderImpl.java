package pl.tk.phishingscanner.infra.amqp.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingTextMessageSender;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.infra.mapping.MessageEventMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhishingTextMessageSenderImpl implements PhishingTextMessageSender {

  private static final String PHISHING_MESSAGES_BINDING = "phishing-messages";
  private final StreamBridge publisher;

  @Override
  public void send(TextMessage message) {
    log.trace("Sending text message to phishing messages queue: {}", message);
    publisher.send(PHISHING_MESSAGES_BINDING, MessageEventMapper.map(message));
  }
}
