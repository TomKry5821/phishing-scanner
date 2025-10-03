package pl.tk.phishingscanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@SpringBootTest
class PhishingScannerApplicationSpec extends Specification {

    @Autowired
    private ApplicationContext context

    void 'Context loads'() {
        expect: 'Context loaded'
        context
    }
}
