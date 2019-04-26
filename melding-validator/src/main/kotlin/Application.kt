package no.ks.fiks.gi.melding

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.SpringBootApplication


fun main(args : Array<String>) {
    SpringApplication.run(SpringBootConsoleApplication::class.java, *args)
}

@SpringBootApplication
open class SpringBootConsoleApplication : CommandLineRunner {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private val job: Runner? = null


    override fun run(args : Array<String>) {
        log.info("EXECUTING : command line runner")
        job?.run()
    }

}
