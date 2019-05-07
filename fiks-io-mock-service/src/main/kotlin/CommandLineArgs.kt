package no.ks.fiks.gi.melding

import com.beust.jcommander.Parameter

class CommandLineArgs{

    @Parameter(names = arrayOf("-config", "-c"))
    var configfile: String = "config.properties"

    @Parameter(names = arrayOf("-svarmappe"))
    var svarMappe: String = "."

    @Parameter(names = ["--help", "-h"], help = true)
    var help: Boolean = false

    override fun toString(): String {
        return "CommandLineArgs(configfile='$configfile', help=$help)"
    }

}