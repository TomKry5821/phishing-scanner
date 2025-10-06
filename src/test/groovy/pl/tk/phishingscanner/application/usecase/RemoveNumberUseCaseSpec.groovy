package pl.tk.phishingscanner.application.usecase

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.application.port.PhishingScannerUserService
import pl.tk.phishingscanner.domain.model.PhoneNumber
import pl.tk.phishingscanner.domain.model.TextMessage
import spock.lang.Specification

import static ProcessUseCaseResult.SUCCESS

@SpringBootTest
class RemoveNumberUseCaseSpec extends Specification {

    private static final PhoneNumber PHONE_NUMBER = new PhoneNumber("48123123123")

    private static final TextMessage TEXT_MESSAGE = new TextMessage(PHONE_NUMBER, PHONE_NUMBER, "Text content with https://example.com")

    @SpringBean
    private PhishingScannerUserService phishingScannerUserService = Mock()

    @Autowired
    private RemoveNumberUseCase subject

    void 'Should not throw error while removing number from scanner service'() {
        given:
        phishingScannerUserService.removeUserByPhoneNumber(_ as PhoneNumber) >> {}

        when:
        def result = subject.execute(TEXT_MESSAGE)

        then:
        result == SUCCESS
    }
}
