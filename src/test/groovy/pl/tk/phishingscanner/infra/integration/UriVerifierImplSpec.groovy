package pl.tk.phishingscanner.infra.integration

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.infra.integration.model.response.Score
import pl.tk.phishingscanner.infra.integration.model.response.VerifyUriResponse
import spock.lang.Specification

import static pl.tk.phishingscanner.infra.integration.model.ConfidenceLevel.HIGH
import static pl.tk.phishingscanner.infra.integration.model.ConfidenceLevel.SAFE
import static pl.tk.phishingscanner.infra.integration.model.ThreatType.SOCIAL_ENGINEERING

@SpringBootTest
class UriVerifierImplSpec extends Specification {

    private static final URI URI = new URI('https://phishing-uri.com')
    private static final VerifyUriResponse PHISHING_RESPONSE = new VerifyUriResponse([new Score(SOCIAL_ENGINEERING, HIGH)] as Set)
    private static final VerifyUriResponse NOT_PHISHING_RESPONSE = new VerifyUriResponse([new Score(SOCIAL_ENGINEERING, SAFE)] as Set)

    @SpringBean
    private UriVerifierClient uriVerifierClient = Mock()

    @Autowired
    private UriVerifierImpl subject

    void 'Should return true for URI that contains phishing'() {
        given:
        uriVerifierClient.retrieveResponse(_ as String) >> {
            return Optional.of(PHISHING_RESPONSE)
        }

        when:
        def result = subject.containsPhishing(URI)

        then:
        result
    }

    void 'Should return false for URI that does not contain phishing'() {
        given:
        uriVerifierClient.retrieveResponse(_ as String) >> {
            return Optional.of(NOT_PHISHING_RESPONSE)
        }

        when:
        def result = subject.containsPhishing(URI)

        then:
        !result
    }

    void 'Should return false while retrieving verification response from URI verify service'() {
        given:
        uriVerifierClient.retrieveResponse(_ as String) >> {
            return Optional.empty()
        }

        when:
        def result = subject.containsPhishing(URI)

        then:
        !result
    }
}
