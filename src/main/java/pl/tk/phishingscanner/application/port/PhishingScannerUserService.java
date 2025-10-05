package pl.tk.phishingscanner.application.port;

import java.util.Optional;
import pl.tk.phishingscanner.domain.model.PhoneNumber;

public interface PhishingScannerUserService {

  Optional<PhoneNumber> addUserByPhoneNumber(PhoneNumber phoneNumber);
}
