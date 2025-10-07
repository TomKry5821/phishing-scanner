package pl.tk.phishingscanner.application.strategy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.application.config.PhishingScannerConfiguration
import pl.tk.phishingscanner.application.usecase.AddNewNumberUseCase
import pl.tk.phishingscanner.application.usecase.ProcessTextMessageUseCase
import pl.tk.phishingscanner.application.usecase.RemoveNumberUseCase
import pl.tk.phishingscanner.application.usecase.strategy.UseCaseStrategy
import pl.tk.phishingscanner.domain.model.PhoneNumber
import pl.tk.phishingscanner.domain.model.TextMessage
import spock.lang.Specification

@SpringBootTest
class UseCaseStrategySpec extends Specification {

    @Autowired
    private UseCaseStrategy subject

    @Autowired
    private PhishingScannerConfiguration phishingScannerConfiguration

    void 'Should get proper use case for text message type'() {
        given:
        def phoneNumber = new PhoneNumber(phishingScannerConfiguration.phoneNumber)
        def message = new TextMessage(phoneNumber, phoneNumber, content)

        when:
        def result = subject.getUseCase(message)

        then:
        result.class == expectedUseCaseClass

        where:
        content           || expectedUseCaseClass
        "START"           || AddNewNumberUseCase
        "STOP"            || RemoveNumberUseCase
        "typical message" || ProcessTextMessageUseCase
    }
}
