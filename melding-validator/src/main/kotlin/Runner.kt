package no.ks.fiks.gi.melding

import org.springframework.stereotype.Component

@Component
class Runner (val reader: MeldingTestReader, val testRunner: TestRunner) {

    public fun run() {
        val tester = reader.getMeldingTester()

        testRunner.run(tester)

    }
}



