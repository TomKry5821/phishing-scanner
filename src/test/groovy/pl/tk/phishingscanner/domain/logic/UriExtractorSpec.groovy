package pl.tk.phishingscanner.domain.logic


import spock.lang.Specification

class UriExtractorSpec extends Specification {

    private static final CORRECT_URIS = [
            'http://example.com',
            'https://example.com/path?query=1#section',
            'https://sub.domain.example.com:8080/path',
            'https://example.com/~user',
    ]
    private static final INCORRECT_URIS = [
            'http://',                         // No host
            '://example.com',                  // No schema
            'http://exa mple.com',             // Invalid white space in domain
            'ht!tp://example.com',             // Invalid protocol
            'example.com',                     // No protocol
            'http://example.com/ /path',       // Invalid white space in path
    ]
    private UriExtractor subject = new UriExtractor();

    void 'Should return correct URIs set'() {
        given:
        def message = "Test message with first URI $firstUri and second URI $secondUri."

        when:
        def result = subject.extractFromMessage(message)

        then:
        result.collect { it.toString() }.containsAll(expectedResult)

        where:
        firstUri          | secondUri         || expectedResult
        CORRECT_URIS[0]   | CORRECT_URIS[1]   || [CORRECT_URIS[0], CORRECT_URIS[1]]
        CORRECT_URIS[2]   | INCORRECT_URIS[0] || [CORRECT_URIS[2]]
        INCORRECT_URIS[1] | INCORRECT_URIS[2] || []
        INCORRECT_URIS[3] | CORRECT_URIS[3]   || [CORRECT_URIS[3]]
        INCORRECT_URIS[4] | INCORRECT_URIS[5] || []

    }
}
