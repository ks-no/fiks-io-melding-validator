package no.ks.fiks.gi.melding.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class ExampleTest : FIKSIOTestHelper() {

    @Test
    @DisplayName("When uploading through the nginx, a dokument with size just below the limit should be succesfully uploaded")
    fun testExampleMessageAndResponse(){
        println("Test running")
        val payload = "{\"number1\":1, \"number2\":3}"
        val filnavn = "add.json"

        val sendMessage = testRunner.sendMessage("testmelding", payload, filnavn)
        val message: Melding = testRunner.getMessageString(sendMessage.meldingId)

        assertEquals(sendMessage.meldingId, message.melding.svarPaMelding)
        val json = testRunner.getJson(message.melding)
        assertTrue(json.contains("result"), json)
        println("Test finished")
    }
}



