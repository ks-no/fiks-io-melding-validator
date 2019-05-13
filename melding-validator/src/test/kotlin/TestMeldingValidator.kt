import no.ks.fiks.gi.melding.SpringBootConsoleApplication
import org.springframework.boot.SpringApplication

class TestMeldingValidator {

    @org.junit.jupiter.api.Test
    internal fun RunValidatorTest() {
        SpringApplication.run(SpringBootConsoleApplication::class.java, "../melding-tester")
    }
}