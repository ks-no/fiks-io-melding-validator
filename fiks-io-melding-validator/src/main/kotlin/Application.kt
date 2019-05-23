package no.ks.fiks.gi.melding

import com.beust.jcommander.JCommander
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.SpringBootApplication


fun main(vararg args : String) {
    SpringApplication.run(SpringBootConsoleApplication::class.java, *args)
}

@SpringBootApplication
open class SpringBootConsoleApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private val job: Runner? = null


    override fun run(vararg args : String) {
        log.info("EXECUTING : command line runner")
        val commandLineArgs = CommandLineArgs()
        JCommander.newBuilder()
                .addObject(commandLineArgs)
                .build().parse(*args)
        println("Running tests in directory " + commandLineArgs.tests)

        job?.run(commandLineArgs.tests)
    }

}
