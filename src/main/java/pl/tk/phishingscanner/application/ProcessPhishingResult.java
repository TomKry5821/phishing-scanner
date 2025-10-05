package pl.tk.phishingscanner.application;

public enum ProcessPhishingResult {
  PHISHING_DETECTED,
  NOT_PHISHING_DETECTED;

  public static ProcessPhishingResult of(boolean phishingDetected) {
    return phishingDetected ? PHISHING_DETECTED : NOT_PHISHING_DETECTED;
  }
}
