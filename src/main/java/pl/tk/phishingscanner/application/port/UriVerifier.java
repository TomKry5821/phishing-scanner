package pl.tk.phishingscanner.application.port;

import java.net.URI;

public interface UriVerifier {
  boolean containsPhishing(URI uri);
}
