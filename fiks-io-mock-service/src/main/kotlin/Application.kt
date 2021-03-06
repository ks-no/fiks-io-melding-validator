package no.ks.fiks.gi.melding

import com.beust.jcommander.JCommander
import no.ks.fiks.gi.melding.io.lib.ConfigProperties
import no.ks.fiks.gi.melding.io.lib.FiksIOServer
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import java.rmi.server.RMISocketFactory.getSocketFactory
import java.security.cert.X509Certificate
import java.util.function.BiConsumer
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


fun main(vararg args : String) {
    SpringApplication.run(SpringBootConsoleApplication::class.java, *args)
}

@SpringBootApplication
open class SpringBootConsoleApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args : String) {
        try {
            log.info("EXECUTING : command line runner")

            val commandLineArgs = CommandLineArgs()
            val jcommander = JCommander.newBuilder()
                    .addObject(commandLineArgs)
                    .build()
            jcommander.parse(*args)

            if (commandLineArgs.help) {
                jcommander.setProgramName("java -jar mock.jar")
                jcommander.usage()
                return
            }
            log.info("Executing with parameters: " + commandLineArgs.toString())
            val configProperties = Properties()
            validateArgs(commandLineArgs)

            configProperties.load(FileInputStream(commandLineArgs.configfile))

            val config = ConfigProperties.load(commandLineArgs.configfile)

            val fiksIOServer = FiksIOServer(config)
            val answers = AnswerReader(commandLineArgs.svarMappe)
            answers.init()
            fiksIOServer.factory.newSubscription { m, v ->
                v.ack()

                try {
                    val zip = m.dekryptertZipStream
                    var entry = zip.nextEntry
                    while (entry != null) {
                        val answerForRequest = answers.getAnswerForRequest((IOUtils.toByteArray(zip)).toString(Charset.forName("UTF-8")))
                        if (answerForRequest != null) {
                            v.svar(m.meldingType, answerForRequest, "answer.json")
                            log.info("Sendte svar på ${m.meldingId} til ${m.avsenderKontoId} med $answerForRequest")
                        }
                        entry = zip.nextEntry
                    }
                }catch(e: Exception) {
                    log.error("Failed to read message $m", e)
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
            System.exit(1)
        }
    }

    private fun findAnswer(byteArray: ByteArray?) {

    }

    private fun validateArgs(commandLineArgs: CommandLineArgs) {
        var errors = ArrayList<String>()
        if (!File(commandLineArgs.configfile).exists()) {
            errors.add("Finner ikke fil '${commandLineArgs.configfile}'")
        }
        if (!File(commandLineArgs.configfile).canRead()) {
            errors.add("Kan ikke lese fil '${commandLineArgs.configfile}'")
        }
        errors.forEach { println(it) }
        if(errors.size > 0)
            System.exit(1)
    }


}
