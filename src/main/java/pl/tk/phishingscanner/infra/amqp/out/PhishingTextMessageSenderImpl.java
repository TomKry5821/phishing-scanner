package pl.tk.phishingscanner.infra.amqp.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingTextMessageSender;
import pl.tk.phishingscanner.domain.model.TextMessage;

@Slf4j
@Component
public class PhishingTextMessageSenderImpl implements PhishingTextMessageSender {

  @Override
  public void send(TextMessage message) {
    log.trace("Sending text message to phishing messages queue: {}", message);
  }
}
