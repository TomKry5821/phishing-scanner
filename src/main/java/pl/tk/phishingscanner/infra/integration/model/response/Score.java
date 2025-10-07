package pl.tk.phishingscanner.infra.integration.model.response;

import pl.tk.phishingscanner.infra.integration.model.ConfidenceLevel;
import pl.tk.phishingscanner.infra.integration.model.ThreatType;

public record Score(ThreatType threatType, ConfidenceLevel confidenceLevel) {}
