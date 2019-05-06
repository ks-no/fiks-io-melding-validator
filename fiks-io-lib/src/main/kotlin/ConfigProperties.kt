package no.ks.fiks.gi.melding.io.lib

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.security.KeyFactory
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

data class ConfigProperties(val virksomhetssertifikat: VirksomhetssertifikatConfig, val kontoconfig: KontoConfig, val idPortenConfig: IdPortenConfig){
    companion object {
        fun load(configproperties:String) : ConfigProperties {
            val props = Properties()
            props.load(FileInputStream(configproperties))

            validateConfigparams(props)

            val config = ConfigProperties(
                    VirksomhetssertifikatConfig(
                            props.getProperty("virksomhetssertifikat.keyalias"),
                            props.getProperty("virksomhetssertifikat.keypassword"),
                            loadKeystore(
                                props.getProperty("virksomhetssertifikat.keystore"),
                                props.getProperty("virksomhetssertifikat.keystorepassword")
                            ),
                            props.getProperty("virksomhetssertifikat.keystorepassword")
                    ),
                    KontoConfig(
                            loadPrivateKey(props.getProperty("konto.privatekey"),
                                props.getProperty("konto.privatekeypassword"))!!,
                            props.getProperty("konto.privatekeypassword"),
                            UUID.fromString(props.getProperty("konto.id")),
                            UUID.fromString(props.getProperty("konto.integrasjonsid")),
                            props.getProperty("konto.integrasjonspassword")

                    ),
                    IdPortenConfig(
                            props.getProperty("idporten.klientid"),
                            "PROD" == props.getProperty("idporten.miljø")
                    ))
            readPrivateKeyFromKeystore(config.virksomhetssertifikat)
            return config
        }

        private fun readPrivateKeyFromKeystore(virksomhetssertifikat: VirksomhetssertifikatConfig) {
            val key = virksomhetssertifikat.keystore.getKey(virksomhetssertifikat.keyalias, virksomhetssertifikat.keypassword.toCharArray())

            if (key == null) {
                println("Klater ikke å lese key fra virksomhetsertifikatkeystore, eksisterende aliases er '" + virksomhetssertifikat.keystore.aliases().toList().joinToString("', '") + "'")
                System.exit(1)
            }
        }

        private fun validateConfigparams(configProperties: Properties) {
            var errors = ArrayList<String>()
            checkFile(errors, configProperties.getProperty("virksomhetssertifikat.keystore"))
            checkFile(errors, configProperties.getProperty("konto.privatekey"))

            try {
                UUID.fromString(configProperties.getProperty("konto.id"))
            } catch (e: Exception ){
                errors.add("Klarte ikke å lese uuid fra '${configProperties.getProperty("konto.id")}'")
            }
            try {
                UUID.fromString(configProperties.getProperty("konto.integrasjonsid"))
            } catch (e: Exception ){
                errors.add("Klarte ikke å lese uuid fra '${configProperties.getProperty("konto.integrasjonsid")}'")
            }

            errors.forEach { println(it) }
            if(errors.size > 0)
                System.exit(1)
        }

        private fun checkFile(errors: ArrayList<String>, file: String?) {

            if (!File(file).exists()) {
                errors.add("Finner ikke fil '${file}'")
            }
            if (!File(file).canRead()) {
                errors.add("Kan ikke lese fil '${file}'")
                System.exit(1)
            }
        }

        private fun loadKeystore(file: String, password: String): KeyStore {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            FileInputStream(file).use { keyStoreData -> keyStore.load(keyStoreData, password.toCharArray()) }
            return keyStore
        }

        private fun loadPrivateKey(keyfile: String, password: String): PrivateKey? {
            val generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(((PEMParser(FileReader(File(keyfile))).readObject()) as PrivateKeyInfo).getEncoded()))
            if(generatePrivate == null) {
                println("Failed to load privatekey, " + keyfile)
                System.exit(1)
                throw RuntimeException("hei")
            } else {
                return generatePrivate
            }
        }
    }
}

data class VirksomhetssertifikatConfig(val keyalias: String, val keypassword: String, val keystore: KeyStore, val keystorepassword: String)

data class KontoConfig(val privatekey: PrivateKey, val privatekeypassword: String, val kontoid: UUID, val integrasjonsid:UUID, val integrasjonspassord: String)

data class IdPortenConfig(val klientid: String, val prod: Boolean)
