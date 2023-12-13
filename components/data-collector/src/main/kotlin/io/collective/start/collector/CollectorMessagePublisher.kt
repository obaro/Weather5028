package io.collective.start.collector

import io.collective.rabbitsupport.PublishAction
import org.json.JSONObject
import org.slf4j.LoggerFactory

class CollectorMessagePublisher(
    private val publishFunction: PublishAction,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun publishNotification(location: String) {
        val message = JSONObject("{\"location\": \"$location\"}")

        logger.info("publishing notification request ${message.toString()}",)
        publishFunction(message.toString())
    }
}