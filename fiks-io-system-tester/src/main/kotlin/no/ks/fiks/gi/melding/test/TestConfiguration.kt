package no.ks.fiks.gi.melding.test

import no.ks.fiks.gi.melding.CommandLineArgs
import no.ks.fiks.gi.melding.Runner
import no.ks.fiks.gi.melding.io.lib.ConfigProperties
import no.ks.fiks.gi.melding.io.lib.FiksIOServer
import no.ks.fiks.io.client.FiksIOKlient
import no.ks.fiks.io.client.model.KontoId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.util.*

@Configuration
open class TestConfiguration {

    @Bean
    open fun commandlineargs(): CommandLineArgs {
        return Runner.commandLineArgs
    }

    @Bean
    open fun mottakerKontoId(): KontoId {
        println("Creating kontoid")
        return KontoId(UUID.fromString(Runner.commandLineArgs.mottaker))
    }

    @Bean
    open fun fiksFactory(): FiksIOKlient {
        val configProperties = Properties()

        configProperties.load(FileInputStream(Runner.commandLineArgs.configfile))

        val config = ConfigProperties.load(Runner.commandLineArgs.configfile)

        val fiksIOServer = FiksIOServer(config)
        return fiksIOServer.factory
    }

    @Bean
    open fun testRunner(ioKlient: FiksIOKlient, mottakerkontoId: KontoId): TestRunner {
        return TestRunner(ioKlient, mottakerkontoId)
    }
}