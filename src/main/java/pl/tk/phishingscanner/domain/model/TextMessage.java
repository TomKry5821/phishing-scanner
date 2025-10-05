package pl.tk.phishingscanner.domain.model;

public record TextMessage(PhoneNumber sender, PhoneNumber receiver, String message) {}
