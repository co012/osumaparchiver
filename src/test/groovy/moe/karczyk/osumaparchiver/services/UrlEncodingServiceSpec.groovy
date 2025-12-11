package moe.karczyk.osumaparchiver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.nio.file.Path

@SpringBootTest
class UrlEncodingServiceSpec extends Specification {
    @Autowired
    private UrlEncodingService urlEncodingService

    def "should correctly encode path"() {
        given:
        def path = Path.of("E:/aa'a(a) - _.png")
        when:
        def encoded = urlEncodingService.encodePath(path)
        then:
        encoded == "file:///E:/aa%27a%28a%29%20-%20_.png"
    }
}
