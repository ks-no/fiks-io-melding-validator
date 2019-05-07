package no.ks.fiks.gi.melding

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File


class AnswerReader(val directory: String) {

    val formattedMessages = HashMap<String,String>()
    val mapper = ObjectMapper()

    fun init() {
        val files = HashMap<String, String>()
        val answers = HashMap<String, String>()
        val requests = HashMap<String,String>()
        File(directory).walk().filter { it.isFile }.forEach {
            files.put(it.name, it.inputStream().readBytes().toString(Charsets.UTF_8)) }
        files.keys.forEach {
            if (it.contains("_answer.json")) {
                val key = it.replace("_answer.json", "")
                answers.put(key, files.get(key + "_answer.json")!!)
            } else if (it.contains("_request.json")){
                val value = it.replace("_request.json", "")

                val map = mapper.readValue(files.get(it), Map::class.java)
                val jsonFormatted = mapper.writeValueAsString(map)
                requests.put(jsonFormatted, value)
            }
        }

        requests.forEach({key,value ->
            formattedMessages.put(key, answers.get(value)!!)
        })
    }

    fun getAnswerForRequest(request: String): String? {
        val formattedjsonrequest = mapper.writeValueAsString(mapper.readValue(request, Map::class.java))
        return formattedMessages[formattedjsonrequest]
    }
}