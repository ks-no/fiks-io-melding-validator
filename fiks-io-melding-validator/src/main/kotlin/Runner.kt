package no.ks.fiks.gi.melding

import org.springframework.stereotype.Component

@Component
class Runner (val reader: MeldingTestReader, val testRunner: TestRunner) {

    public fun run(testpath: String) {
        val tester = reader.getMeldingTester(testpath)

        testRunner.run(tester)

    }
}



