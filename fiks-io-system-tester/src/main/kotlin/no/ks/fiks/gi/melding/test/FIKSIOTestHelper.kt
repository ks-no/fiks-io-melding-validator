package no.ks.fiks.gi.melding.test

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@Execution(ExecutionMode.CONCURRENT)
@ExtendWith(SpringExtension::class)
@Import(TestConfiguration::class)
open class FIKSIOTestHelper {

    @Autowired
    private var tr: TestRunner? = null

    val testRunner: TestRunner
        get() = tr!!
}

