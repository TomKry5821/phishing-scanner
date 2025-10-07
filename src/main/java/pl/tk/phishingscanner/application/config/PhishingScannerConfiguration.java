package pl.tk.phishingscanner.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "phishing-scanner")
public class PhishingScannerConfiguration {

  /**
   * Numer telefonu w formacie 48123123123, na który przychodzą wiadomość włączające lub wyłączające
   * usługę skanowania.
   */
  private String phoneNumber;
}
