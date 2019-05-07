package no.ks.fiks.gi.melding

import com.beust.jcommander.JCommander
import no.ks.fiks.gi.melding.Runner.Companion.commandLineArgs
import no.ks.fiks.gi.melding.io.lib.ConfigProperties
import no.ks.fiks.gi.melding.io.lib.FiksIOServer
import no.ks.fiks.gi.melding.test.TestConfiguration
import no.ks.fiks.io.client.FiksIOKlient
import no.ks.fiks.io.client.FiksIOKlientFactory
import no.ks.fiks.io.client.model.KontoId
import no.ks.fiks.io.client.model.MeldingRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherFactory
import org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns
import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.listeners.SummaryGeneratingListener
import java.io.PrintWriter
import org.junit.platform.launcher.listeners.TestExecutionSummary




class Runner {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)

        fun main(vararg args: String) {

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
            validateArgs(commandLineArgs)

            SpringApplication.run(SpringBootConsoleApplication::class.java, *args)
        }

        val commandLineArgs = CommandLineArgs();

        private fun validateArgs(commandLineArgs: CommandLineArgs) {
            var errors = ArrayList<String>()
            if (!File(commandLineArgs.configfile).exists()) {
                errors.add("Finner ikke fil '${commandLineArgs.configfile}'")
            }
            if (!File(commandLineArgs.configfile).canRead()) {
                errors.add("Kan ikke lese fil '${commandLineArgs.configfile}'")
            }
            try {
                UUID.fromString(commandLineArgs.mottaker)
            } catch (e: Exception) {
                errors.add("Klarte ikke å lese mottaker fra '${commandLineArgs.mottaker}'")
            }
            errors.forEach { println(it) }
            if (errors.size > 0)
                System.exit(1)
        }
    }


    fun main(vararg args: String) {
        main(*args)
    }


}

@SpringBootApplication(scanBasePackages = ["no.noexisting"])
open class SpringBootConsoleApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    val listener = SummaryGeneratingListener()

    override fun run(vararg args: String) {
        log.info("EXECUTING : command line runner")


        val request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("no.ks.fiks.gi.melding.test"))
                .filters(includeClassNamePatterns(".*Test"))

                .build()
        val launcher = LauncherFactory.create()
        val testPlan = launcher.discover(request)
        launcher.registerTestExecutionListeners(listener)
        launcher.execute(request)
        listener.summary.printFailuresTo(PrintWriter(System.out))
        val summary = listener.summary
        summary.printTo(PrintWriter(System.out))
        if(listener.summary.testsFailedCount > 0){
            System.exit(10)
        }
        System.exit(0)
    }


}
