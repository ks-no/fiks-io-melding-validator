package no.ks.fik.io.mock

import no.ks.fiks.gi.melding.SpringBootConsoleApplication
import org.springframework.boot.SpringApplication

fun main(args : Array<String>) {
    SpringApplication.run(SpringBootConsoleApplication::class.java, "-config", "ignored/arkivsystemmock/mock.properties", "-svarmappe", "mockserver-meldinger")
}