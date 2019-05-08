package no.ks.fiks.gi.melding.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch


class PerformanceTest : FIKSIOTestHelper() {

    @Test
    @DisplayName("Submitting many messages performance test")
    fun testPerformance(){

        val payload = "{\"number1\":1, \"number2\":3}"
        val filnavn = "add.json"
        val nrmessages = 1;
        val watch = StopWatch()
        watch.start()
        val sendMessage = (1.. nrmessages).map { testRunner.sendMessage("testmelding", payload, filnavn) }.toList()
        watch.stop()
        println("Sending av $nrmessages tok ${watch.totalTimeMillis} for hver melding ${watch.totalTimeMillis / nrmessages}")
        val messages = sendMessage.map { testRunner.getMessageString(it.meldingId) }.toList()

        sendMessage.forEachIndexed { index, sendtMelding ->
            assertEquals(sendtMelding.meldingId, messages[index].melding.svarPaMelding)
            val json = testRunner.getJson(messages[index].melding)
            assertTrue(json.contains("result"), json)
        }

        println("Test finished")
    }
}