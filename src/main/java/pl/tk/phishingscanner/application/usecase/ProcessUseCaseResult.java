package pl.tk.phishingscanner.application.usecase;

public enum ProcessUseCaseResult {
  SUCCESS,
  FAILURE;

  public static ProcessUseCaseResult of(boolean isSuccess) {
    return isSuccess ? SUCCESS : FAILURE;
  }
}
