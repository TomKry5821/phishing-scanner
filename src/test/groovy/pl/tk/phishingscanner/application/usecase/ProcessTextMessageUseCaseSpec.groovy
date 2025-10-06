package pl.tk.phishingscanner.application.usecase

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.application.port.PhishingScannerUserService
import pl.tk.phishingscanner.application.port.PhishingTextMessageSender
import pl.tk.phishingscanner.application.port.SafeTextMessageSender
import pl.tk.phishingscanner.application.port.UriVerifier
import pl.tk.phishingscanner.domain.model.PhoneNumber
import pl.tk.phishingscanner.domain.model.TextMessage
import spock.lang.Specification

import static ProcessUseCaseResult.FAILURE
import static ProcessUseCaseResult.SUCCESS

@SpringBootTest
class ProcessTextMessageUseCaseSpec extends Specification {

    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("48123123123")

    private static final TextMessage TEXT_MESSAGE = new TextMessage(PHONE_NUMBER, PHONE_NUMBER, "Text content with https://example.com")

    @SpringBean
    private UriVerifier uriVerifier = Mock()

    @SpringBean
    private PhishingScannerUserService phishingScannerUserService = Mock()

    @SpringBean
    private PhishingTextMessageSender phishingTextMessageSender = Mock()

    @SpringBean
    private SafeTextMessageSender safeTextMessageSender = Mock()

    @Autowired
    private ProcessTextMessageUseCase subject

    void 'Should return failure while processing text message'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> true
        phishingScannerUserService.isUser(_ as PhoneNumber) >> true
        phishingTextMessageSender.send(_ as TextMessage) >> {}

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == FAILURE
    }

    void 'Should return success while processing text message'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> false
        phishingScannerUserService.isUser(_ as PhoneNumber) >> true
        safeTextMessageSender.send(_ as TextMessage) >> {}

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == SUCCESS
    }

    void 'Should return success while processing text message with phishing for not scanner user'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> true
        phishingScannerUserService.isUser(_ as PhoneNumber) >> false
        phishingTextMessageSender.send(_ as TextMessage) >> {}

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == SUCCESS
    }
}
