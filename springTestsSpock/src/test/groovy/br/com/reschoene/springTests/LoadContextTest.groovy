package br.com.reschoene.springTests

import br.com.reschoene.springTests.controllers.MovieController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class LoadContextTest extends Specification {

    @Autowired (required = false)
    private MovieController movieController

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        movieController
    }
}