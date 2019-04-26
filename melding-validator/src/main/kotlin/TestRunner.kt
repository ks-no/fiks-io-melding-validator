package no.ks.fiks.gi.melding

import org.leadpony.justify.api.JsonValidationService
import org.springframework.stereotype.Component
import java.io.FileInputStream


@Component
class TestRunner() {
    val service = JsonValidationService.newInstance()

    fun run( tests: List<MeldingTest>){
        tests.forEach {meldingtest ->
            println(meldingtest.navn + ": Tester meldinger mot " + meldingtest.jsonSpec)
            println()

            meldingtest.meldinger?.forEach { melding ->
                println()
                println(meldingtest.navn + ": Tester melding " +melding)

                val schema = service.readSchema(FileInputStream(meldingtest.jsonSpec))
                val errors : MutableList<String> = ArrayList();
// Problem handler which will print problems found.
                val handler = service.createProblemPrinter({ errors.add(it) })

                service.createParser(FileInputStream(melding), schema, handler).use { parser ->
                    while (parser.hasNext()) {
                        val event = parser.next()
                        // Do something useful here
                    }
                }
                if(errors.size > 0) {
                    println(meldingtest.navn + ": Errors:")
                    errors.forEach { println(meldingtest.navn + ": " + it) }
                } else
                    println(meldingtest.navn + ": Ingen error i " + melding)
            }

        }
    }
}