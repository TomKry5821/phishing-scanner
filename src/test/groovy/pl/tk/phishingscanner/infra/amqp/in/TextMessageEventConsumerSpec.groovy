package pl.tk.phishingscanner.infra.amqp.in

import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.EnableTestBinder
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import pl.tk.phishingscanner.application.port.PhishingScannerUserService
import pl.tk.phishingscanner.application.port.UriVerifier
import pl.tk.phishingscanner.domain.model.PhoneNumber
import pl.tk.phishingscanner.infra.amqp.in.event.NewTextMessageEvent
import spock.lang.Specification

import static org.springframework.messaging.support.MessageBuilder.withPayload

@SpringBootTest
@EnableTestBinder
class TextMessageEventConsumerSpec extends Specification {

    private static final String NEW_TEXT_MESSAGE_BINDING = "new-message"
    private static final String SAFE_MESSAGE_BINDING = "safe-messages"
    private static final String PHISHING_MESSAGES_BINDING = "phishing-messages"

    @SpringBean
    private UriVerifier uriVerifier = Mock()

    @SpringBean
    private PhishingScannerUserService phishingScannerUserService = Mock()

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private InputDestination input

    @Autowired
    private OutputDestination output

    void 'Should receive and handle new text message event without phishing'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> false
        phishingScannerUserService.isUser(_ as PhoneNumber) >> true
        def event = new NewTextMessageEvent("48123123123", "48123123123", "Test message with https://test.com")

        when:
        output.clear()
        input.send(withPayload(objectMapper.writeValueAsBytes(event)).build(), NEW_TEXT_MESSAGE_BINDING)

        then:
        noExceptionThrown()
        output.receive(100L, SAFE_MESSAGE_BINDING)
    }

    void 'Should receive and handle new text message event with phishing'() {
        given:
        uriVerifier.containsPhishing(_ as URI) >> true
        phishingScannerUserService.isUser(_ as PhoneNumber) >> true
        def event = new NewTextMessageEvent("48123123123", "48123123123", "Test message with https://test.com")

        when:
        output.clear()
        input.send(withPayload(objectMapper.writeValueAsBytes(event)).build(), NEW_TEXT_MESSAGE_BINDING)

        then:
        noExceptionThrown()
        output.receive(100L, PHISHING_MESSAGES_BINDING)
    }

    void 'Should receive and not handle invalid event'() {
        given:
        def event = 12L

        when:
        output.clear()
        input.send(withPayload(objectMapper.writeValueAsBytes(event)).build(), NEW_TEXT_MESSAGE_BINDING)

        then:
        noExceptionThrown()
        !output.receive(10L, SAFE_MESSAGE_BINDING)
        !output.receive(10L, PHISHING_MESSAGES_BINDING)
    }
}
