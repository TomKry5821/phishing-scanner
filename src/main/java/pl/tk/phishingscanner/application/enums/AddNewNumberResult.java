package pl.tk.phishingscanner.application.enums;

public enum AddNewNumberResult {
  SUCCESS,
  FAILURE;

  public static AddNewNumberResult of(boolean phoneNumberAdded) {
    return phoneNumberAdded ? SUCCESS : FAILURE;
  }
}
