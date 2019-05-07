package no.ks.fiks.gi.melding.test

import no.ks.fiks.io.client.FiksIOKlient
import no.ks.fiks.io.client.SvarSender
import no.ks.fiks.io.client.model.*
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class TestRunner(val ioKlient: FiksIOKlient, val mottakerkontoId: KontoId) {

    val messages = LinkedBlockingQueueMap<String, Melding>()

    private val acceptMessage: (MottattMelding, SvarSender) -> Unit = {mottattMelding, svarSender ->
        messages.offer(mottattMelding.svarPaMelding.toString(), Melding(mottattMelding, svarSender) )
        svarSender.ack()
    }
    val subscription = ioKlient.newSubscription(acceptMessage)

    fun sendMessage(meldingstype: String, payload: String, filnavn: String): SendtMelding {
        val request = MeldingRequest.builder().ttl(Duration.ofSeconds(30)).meldingType(meldingstype).mottakerKontoId(mottakerkontoId).build()
        return ioKlient.send(request, payload, filnavn)!!
    }

    fun getMessageString(meldingId: MeldingId): Melding {
        return messages.get(meldingId.toString())
    }

    val charset = Charset.forName("UTF-8")

    fun getJson(melding: MottattMelding): String {

        val dekryptertZipStream = melding.dekryptertZipStream
        var nextEntry = dekryptertZipStream.nextEntry
        while(nextEntry != null) {
            println("fiule" + nextEntry.name)
            val bytes = IOUtils.toByteArray(dekryptertZipStream)
            return String(bytes, charset)
            nextEntry = dekryptertZipStream.nextEntry
        }
        return ""
    }


}

interface Tester {
    fun run(ioKlient: FiksIOKlient, svarSender: SvarSender, melding: Melding )
}

data class Melding(val melding: MottattMelding, val svarSender: SvarSender)

class LinkedBlockingQueueMap<K,V> {
    val map = ConcurrentHashMap<K, LinkedBlockingQueue<V>>()

    fun getMapValue(k: K): LinkedBlockingQueue<V> {
        if(map.containsKey(k))
            return map.get(k)!!
        map.putIfAbsent(k, LinkedBlockingQueue())
        return map.get(k)!!
    }

    fun offer(k: K, v: V) {
        val queue = getMapValue(k)
        queue.offer(v)
    }

    fun get(k: K): V {
        val queue = getMapValue(k)
        return queue.poll(30, TimeUnit.SECONDS)
    }
}