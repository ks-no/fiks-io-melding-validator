package no.ks.fiks.gi.melding

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import java.io.File
import java.util.stream.Collectors

@Component
class MeldingTestReader{

    fun getMeldingTester(): List<MeldingTest>  {
        val specfiles = HashMap<String, File>()
        val meldingFiler = LinkedMultiValueMap<String, File>()
        val file = File("melding-tester")

        file.walk().forEach {
            if(it.isFile){

                if(it.path.contains("spec")){
                    val key = it.parentFile.parentFile.name
                    specfiles.put(key, it)
                } else {
                    val key = it.parentFile.name
                    meldingFiler.add(key, it)
                }
            }
        }
        return specfiles.keys.stream().map { key -> MeldingTest(key, specfiles.get(key), meldingFiler.get(key)) }.collect(Collectors.toList())
    }


}

data class MeldingTest (val navn: String, val jsonSpec: File?, val meldinger: List<File>? )
