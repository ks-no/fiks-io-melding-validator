package no.ks.fiks.gi.melding

import com.beust.jcommander.Parameter

class CommandLineArgs{

    @Parameter(names = arrayOf("-config", "-c"))
    var configfile: String = "config.properties"

    @Parameter(names = arrayOf("-mottaker", "-m"))
    var mottaker: String = ""

    @Parameter(names = ["--help", "-h"], help = true)
    var help: Boolean = false

    override fun toString(): String {
        return "CommandLineArgs(configfile='$configfile', mottaker='$mottaker', help=$help)"
    }


}