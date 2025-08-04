package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.config.AppProperties
import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

@Service
class XmlReader(private val props: AppProperties) : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun readSwiftRecords(): List<SwiftRecord> {
        val records = mutableListOf<SwiftRecord>()
        logger.info("Reading Swift records from XML.")

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val inputStream = ClassPathResource("/${props.xmlFile}").inputStream

        if (inputStream.available() == 0) {
            logger.warn("XML file is empty.")
            return emptyList()
        }

        val document = documentBuilder.parse(inputStream)

        document.documentElement.normalize()
        val nodeList = document.getElementsByTagName("record")
        logger.info("Found ${nodeList.length} records in XML.")


        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                parseRecord(node as Element)?.let { records.add(it) }
            }
        }
        logger.info("Successfully read ${records.size} valid records from XML.")
        return records
    }

    private fun parseRecord(element: Element): SwiftRecord? {
        val reference = element.getAttribute("reference").trim()
        val accountNumber = getContentOrNull(element, "accountNumber")
        val description = getContentOrNull(element, "description") ?: ""
        val startBalance = getDoubleOrNull(element, "startBalance")
        val mutation = getDoubleOrNull(element, "mutation")
        val endBalance = getDoubleOrNull(element, "endBalance")

        return if (
            reference.isBlank() ||
            accountNumber == null ||
            startBalance == null ||
            mutation == null ||
            endBalance == null
        ) {
            logger.warn("Skipping malformed XML record: reference=$reference, accountNumber=$accountNumber, startBalance=$startBalance, mutation=$mutation, endBalance=$endBalance")
            null
        } else {
            SwiftRecord(reference, accountNumber, description, startBalance, mutation, endBalance)
        }
    }

    private fun getContentOrNull(element: Element, tagName: String): String? {
        val nodeList = element.getElementsByTagName(tagName)
        if (nodeList.length == 0) return null

        val node = nodeList.item(0) ?: return null
        val content = node.textContent?.trim()

        if (content.isNullOrBlank()) {
            return null
        }
        return content
    }


    private fun getDoubleOrNull(element: Element, tagName: String): Double? =
        getContentOrNull(element, tagName)?.toDoubleOrNull()
}
