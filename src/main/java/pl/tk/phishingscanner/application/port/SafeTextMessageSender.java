package pl.tk.phishingscanner.application.port;

import pl.tk.phishingscanner.domain.model.TextMessage;

public interface SafeTextMessageSender {
  void send(TextMessage message);
}
