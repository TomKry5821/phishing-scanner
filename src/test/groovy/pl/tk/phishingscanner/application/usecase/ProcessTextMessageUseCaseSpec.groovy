package pl.tk.phishingscanner.application.usecase

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.application.port.UriVerifier
import pl.tk.phishingscanner.domain.model.PhoneNumber
import pl.tk.phishingscanner.domain.model.TextMessage
import spock.lang.Specification

import static pl.tk.phishingscanner.application.ProcessPhishingResult.NOT_PHISHING_DETECTED
import static pl.tk.phishingscanner.application.ProcessPhishingResult.PHISHING_DETECTED

@SpringBootTest
class ProcessTextMessageUseCaseSpec extends Specification {

    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("48123123123")

    private static final TextMessage TEXT_MESSAGE = new TextMessage(PHONE_NUMBER, PHONE_NUMBER, "Text message with https://example.com")

    @SpringBean
    private UriVerifier uriVerifier = Mock()

    @Autowired
    private ProcessTextMessageUseCase subject

    void 'Should return PHISHING_DETECTED while processing text message'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> true

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == PHISHING_DETECTED
    }

    void 'Should return PHISHING_DETECTED while processing text message'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> false

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == NOT_PHISHING_DETECTED
    }
}
