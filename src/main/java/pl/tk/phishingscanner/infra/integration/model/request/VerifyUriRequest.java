package pl.tk.phishingscanner.infra.integration.model.request;

import static pl.tk.phishingscanner.infra.integration.model.ThreatType.SOCIAL_ENGINEERING;

import pl.tk.phishingscanner.infra.integration.model.ThreatType;

public record VerifyUriRequest(String uri, ThreatType threatType, boolean allowScan) {
  public VerifyUriRequest(String uri) {
    this(uri, SOCIAL_ENGINEERING, true);
  }
}
