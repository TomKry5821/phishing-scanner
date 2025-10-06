package pl.tk.phishingscanner.application.strategy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.application.usecase.AddNewNumberUseCase
import pl.tk.phishingscanner.application.usecase.ProcessTextMessageUseCase
import pl.tk.phishingscanner.application.usecase.RemoveNumberUseCase
import pl.tk.phishingscanner.application.usecase.strategy.UseCaseStrategy
import spock.lang.Specification

import static pl.tk.phishingscanner.domain.model.TextMessageType.ADD_NUMBER_FOR_SCAN
import static pl.tk.phishingscanner.domain.model.TextMessageType.REMOVE_NUMBER_FROM_SCAN
import static pl.tk.phishingscanner.domain.model.TextMessageType.SCAN_MESSAGE

@SpringBootTest
class UseCaseStrategySpec extends Specification {

    @Autowired
    private UseCaseStrategy subject

    void 'Should get proper use case for text message type'() {
        given:
        when:
        def result = subject.getUseCase(textMessageType)

        then:
        result.class == expectedUseCaseClass

        where:
        textMessageType         || expectedUseCaseClass
        ADD_NUMBER_FOR_SCAN     || AddNewNumberUseCase
        REMOVE_NUMBER_FROM_SCAN || RemoveNumberUseCase
        SCAN_MESSAGE            || ProcessTextMessageUseCase
    }
}
