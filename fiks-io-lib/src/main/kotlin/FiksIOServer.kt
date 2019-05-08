package no.ks.fiks.gi.melding.io.lib

import no.ks.fiks.io.client.FiksIOKlientFactory
import no.ks.fiks.io.client.konfigurasjon.*
import no.ks.fiks.io.client.model.KontoId


class FiksIOServer(val config: ConfigProperties) {

    private val amqpkonfigurasjon: AmqpKonfigurasjon = AmqpKonfigurasjon.builder().host("io.fiks.dev.ks.no").port(5671).trustAllSSLCertificates(true).build()
    private val virksomhetsertifikatkonfigurasjon: VirksomhetssertifikatKonfigurasjon = VirksomhetssertifikatKonfigurasjon.builder()
            .keyAlias(config.virksomhetssertifikat.keyalias)
            .keyPassword(config.virksomhetssertifikat.keypassword)
            .keyStore(config.virksomhetssertifikat.keystore)
            .keyStorePassword(config.virksomhetssertifikat.keystorepassword).build()

    private val idPortenConfig = (if (config.idPortenConfig.prod) IdPortenKonfigurasjon.PROD else IdPortenKonfigurasjon.VER2)
            .klientId(config.idPortenConfig.klientid).build()

    private val kontokonfigurasjon: KontoKonfigurasjon = KontoKonfigurasjon.builder()
            .kontoId(KontoId(config.kontoconfig.kontoid))
            .privatNokkel(config.kontoconfig.privatekey)
            .build()

    private val fiksIntegrasjonKonto: FiksIntegrasjonKonfigurasjon = FiksIntegrasjonKonfigurasjon.builder().idPortenKonfigurasjon(idPortenConfig)
            .integrasjonId(config.kontoconfig.integrasjonsid)
            .integrasjonPassord(config.kontoconfig.integrasjonspassord).build()
    val factory = FiksIOKlientFactory.build(FiksIOKonfigurasjon.builder()
            .fiksApiKonfigurasjon(FiksApiKonfigurasjon.TEST)
            .virksomhetssertifikatKonfigurasjon(virksomhetsertifikatkonfigurasjon)
            .kontoKonfigurasjon(kontokonfigurasjon)
            .amqpKonfigurasjon(amqpkonfigurasjon)
            .fiksIntegrasjonKonfigurasjon(fiksIntegrasjonKonto).build())


}