package no.ks.fiks.gi.melding

import com.beust.jcommander.Parameter

class CommandLineArgs{

    @Parameter
    var tests: String = "melding-tester"

    override fun toString(): String {
        return "CommandLineArgs(tests='$tests')"
    }


}