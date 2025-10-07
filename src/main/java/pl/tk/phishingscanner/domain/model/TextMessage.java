package pl.tk.phishingscanner.domain.model;

import static pl.tk.phishingscanner.domain.model.TextMessageType.ADD_NUMBER_FOR_SCAN;
import static pl.tk.phishingscanner.domain.model.TextMessageType.REMOVE_NUMBER_FROM_SCAN;
import static pl.tk.phishingscanner.domain.model.TextMessageType.SCAN_MESSAGE;

public record TextMessage(
    PhoneNumber sender, PhoneNumber receiver, String content, TextMessageType type) {

  public TextMessage(PhoneNumber sender, PhoneNumber receiver, String content) {
    this(sender, receiver, content, getMessageType(content));
  }

  private static TextMessageType getMessageType(String content) {
    return switch (content) {
      case "START" -> ADD_NUMBER_FOR_SCAN;
      case "STOP" -> REMOVE_NUMBER_FROM_SCAN;
      default -> SCAN_MESSAGE;
    };
  }
}
