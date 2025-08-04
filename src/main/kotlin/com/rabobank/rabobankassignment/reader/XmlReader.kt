package com.rabobank.rabobankassignment.reader

import com.rabobank.rabobankassignment.model.SwiftRecord
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

@Service
class XmlReader : SwiftRecordReader {

    private val logger = LoggerFactory.getLogger(XmlReader::class.java)

    override fun readSwiftRecords(): List<SwiftRecord> {
        val records = mutableListOf<SwiftRecord>()
        logger.info("Reading Swift records from XML.")

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(ClassPathResource("/records.xml").inputStream)

        document.documentElement.normalize()
        val nodeList = document.getElementsByTagName("record")
        logger.info("Found ${nodeList.length} records in XML.")

        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element
                val swiftRecord = SwiftRecord(
                    reference = element.getAttribute("reference"),
                    accountNumber = getContent(element, "accountNumber"),
                    description = getContent(element, "description"),
                    startBalance = getContent(element, "startBalance").toDouble(),
                    mutation = getContent(element, "mutation").toDouble(),
                    endBalance = getContent(element, "endBalance").toDouble()
                )
                records.add(swiftRecord)
            }
        }

        logger.info("Successfully read ${records.size} records from XML.")
        return records
    }

    private fun getContent(element: Element, tagName: String): String =
        element.getElementsByTagName(tagName).item(0).textContent
}
