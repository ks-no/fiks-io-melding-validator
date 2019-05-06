package no.ks.fiks.gi.melding

import com.beust.jcommander.JCommander
import no.ks.fiks.gi.melding.io.lib.ConfigProperties
import no.ks.fiks.gi.melding.io.lib.FiksIOServer
import no.ks.fiks.io.client.model.KontoId
import no.ks.fiks.io.client.model.MeldingRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList


fun main(vararg args : String) {
    SpringApplication.run(SpringBootConsoleApplication::class.java, *args)
}

@SpringBootApplication
open class SpringBootConsoleApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)



    override fun run(vararg args : String) {
        log.info("EXECUTING : command line runner")

        val commandLineArgs = CommandLineArgs()
        val jcommander = JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build()
        jcommander.parse(*args)

        if(commandLineArgs.help){
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
        val reqeust = MeldingRequest.builder().meldingType("testmelding")
                .mottakerKontoId(KontoId(UUID.fromString("a9679553-06e7-4931-adec-28d2eaf941ba")))
                .build()
        val payload = "{\"number1\":1, \"number2\":3}"
        val filnavn = "add.json"
        fiksIOServer.factory.send( reqeust, payload, filnavn)

        fiksIOServer.factory.close()
        System.exit(0)
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
