package no.ks.fik.io.mock

import no.ks.fiks.gi.melding.Runner
import no.ks.fiks.gi.melding.SpringBootConsoleApplication
import org.springframework.boot.SpringApplication

fun main(args : Array<String>) {
    Runner.main("-config", "ignored/validator/mock.properties", "-m", "32498c2f-8985-4b4a-8227-25e3ce88e7fc")
}