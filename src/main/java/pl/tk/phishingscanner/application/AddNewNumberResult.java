package pl.tk.phishingscanner.application;

public enum AddNewNumberResult {
  SUCCESS,
  FAILURE;

  public static AddNewNumberResult of(boolean phoneNumberAdded) {
    return phoneNumberAdded ? SUCCESS : FAILURE;
  }
}
