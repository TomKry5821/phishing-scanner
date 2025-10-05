package pl.tk.phishingscanner.domain.model;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneNumber {

  private int countryCode;

  private long number;

  public PhoneNumber(String rawPhoneNumber) {
    try {
      Phonenumber.PhoneNumber parsedNumber =
          PhoneNumberUtil.getInstance().parse("+" + rawPhoneNumber, null);
      this.countryCode = parsedNumber.getCountryCode();
      this.number = parsedNumber.getNationalNumber();
    } catch (NumberParseException e) {
      throw new IllegalArgumentException(
          "Invalid phone number format: %s".formatted(rawPhoneNumber));
    }
  }
}
