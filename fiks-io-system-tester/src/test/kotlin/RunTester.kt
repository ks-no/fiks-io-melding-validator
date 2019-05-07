package no.ks.fik.io.mock

import no.ks.fiks.gi.melding.Runner
import no.ks.fiks.gi.melding.SpringBootConsoleApplication
import org.springframework.boot.SpringApplication

fun main(args : Array<String>) {
    Runner.main("-config", "ignored/tester/mock.properties", "-m", "a9679553-06e7-4931-adec-28d2eaf941ba")
}