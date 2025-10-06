package pl.tk.phishingscanner.infra.amqp.in.event;

public record NewTextMessageEvent(String sender, String receiver, String content) {}
