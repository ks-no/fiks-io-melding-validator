package no.ks.fiks.gi.melding

import org.springframework.stereotype.Component

@Component
class Runner (val reader: MeldingTestReader, val testRunner: TestRunner) {

    fun run(testpath: String):Int {
        val tester = reader.getMeldingTester(testpath)

        return testRunner.run(tester)
    }
}



