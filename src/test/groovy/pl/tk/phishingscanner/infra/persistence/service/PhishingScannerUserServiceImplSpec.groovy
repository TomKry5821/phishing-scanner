package pl.tk.phishingscanner.infra.persistence.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.tk.phishingscanner.domain.model.PhoneNumber
import spock.lang.Specification

@SpringBootTest
class PhishingScannerUserServiceImplSpec extends Specification {

    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("48123123123")

    @Autowired
    private PhishingScannerUserServiceImpl subject

    void 'Should add new user to db'() {
        given:
        when:
        def result = subject.addUserByPhoneNumber(PHONE_NUMBER)

        then:
        result.get() == PHONE_NUMBER
    }

    void 'Should not throw error while adding existing user to db'() {
        given:
        subject.addUserByPhoneNumber(PHONE_NUMBER)

        when:
        def result = subject.addUserByPhoneNumber(PHONE_NUMBER)

        then:
        result.get() == PHONE_NUMBER
    }
}
