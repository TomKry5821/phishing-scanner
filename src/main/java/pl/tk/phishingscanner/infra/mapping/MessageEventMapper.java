package pl.tk.phishingscanner.infra.mapping;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.domain.model.TextMessage;
import pl.tk.phishingscanner.infra.amqp.in.event.NewTextMessageEvent;

@Slf4j
public final class MessageEventMapper {

  public static Optional<TextMessage> map(NewTextMessageEvent source) {
    try {
      return Optional.of(
          new TextMessage(
              new PhoneNumber(source.sender()),
              new PhoneNumber(source.receiver()),
              source.content()));
    } catch (IllegalArgumentException e) {
      log.warn("Problem while mapping event: {}, {} {}", source, e.getMessage(), e.getClass());
      return Optional.empty();
    }
  }

  public static NewTextMessageEvent map(TextMessage source) {
    return new NewTextMessageEvent(
        source.sender().getNumberAsString(),
        source.receiver().getNumberAsString(),
        source.content());
  }
}
