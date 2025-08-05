package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

@Component
class XmlReader(private val props: AppProperties) : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun readSwiftRecords(): Sequence<SwiftRecord> {
        logger.info("Reading Swift records from XML.")

        val resource = ClassPathResource("/${props.xmlFile}")
        if (!resource.exists()) {
            logger.warn("XML file not found.")
            return emptySequence()
        }

        val inputStream = resource.inputStream
        if (inputStream.available() == 0) {
            logger.warn("XML file is empty.")
            return emptySequence()
        }

        val reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream)

        return sequence {
            var currentElement: String? = null
            var reference: String? = null
            var accountNumber: String? = null
            var description = ""
            var startBalance: Double? = null
            var mutation: Double? = null
            var endBalance: Double? = null

            while (reader.hasNext()) {
                val event = reader.next()
                when (event) {
                    XMLStreamConstants.START_ELEMENT -> {
                        currentElement = reader.localName
                        if (currentElement == "record") {
                            reference = reader.getAttributeValue(null, "reference")?.trim()
                            accountNumber = null
                            description = ""
                            startBalance = null
                            mutation = null
                            endBalance = null
                        }
                    }

                    XMLStreamConstants.CHARACTERS -> {
                        val text = reader.text.trim()
                        if (text.isNotEmpty()) {
                            when (currentElement) {
                                "accountNumber" -> accountNumber = text
                                "description" -> description = text
                                "startBalance" -> startBalance = text.toDoubleOrNull()
                                "mutation" -> mutation = text.toDoubleOrNull()
                                "endBalance" -> endBalance = text.toDoubleOrNull()
                            }
                        }
                    }

                    XMLStreamConstants.END_ELEMENT -> {
                        if (reader.localName == "record") {
                            if (
                                !reference.isNullOrBlank() &&
                                accountNumber != null &&
                                startBalance != null &&
                                mutation != null &&
                                endBalance != null
                            ) {
                                yield(
                                    SwiftRecord(
                                        reference,
                                        accountNumber,
                                        description,
                                        startBalance,
                                        mutation,
                                        endBalance
                                    )
                                )
                            } else {
                                logger.warn("Skipping malformed XML record: reference=$reference")
                            }
                        }
                    }
                }
            }
            logger.info("Done reading XML stream.")
        }
    }
}
