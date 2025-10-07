package pl.tk.phishingscanner.domain.logic;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UriExtractor {

  private static final String URI_REGEX = "\\b(https?://\\S*?)(?=[.?!,;:]?(\\s|$))";
  private static final Pattern URI_PATTERN = Pattern.compile(URI_REGEX, CASE_INSENSITIVE);

  public Set<URI> extractFromMessage(String message) {
    return Optional.ofNullable(message).map(UriExtractor::extract).orElseGet(Set::of);
  }

  private static Set<URI> extract(String message) {
    Matcher matcher = URI_PATTERN.matcher(message);
    return matcher
        .results()
        .map(UriExtractor::getUri)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  private static Optional<URI> getUri(MatchResult result) {
    String uri = result.group(1);
    try {
      return Optional.of(new URI(uri));
    } catch (URISyntaxException e) {
      log.debug("Ignoring incorrect URI: {}", uri);
      return Optional.empty();
    }
  }
}
