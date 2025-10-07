package pl.tk.phishingscanner.infra.integration.model.response;

import java.util.Set;

public record VerifyUriResponse(Set<Score> scores) {}
