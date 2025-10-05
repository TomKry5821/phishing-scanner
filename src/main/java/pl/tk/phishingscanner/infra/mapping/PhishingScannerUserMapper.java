package pl.tk.phishingscanner.infra.mapping;

import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.infra.persistence.entity.PhishingScannerUser;

public final class PhishingScannerUserMapper {

  public static PhishingScannerUser map(PhoneNumber source) {
    var user = new PhishingScannerUser();
    user.setPhoneNumber(source.getNumberAsString());
    return user;
  }
}
