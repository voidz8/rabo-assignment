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

    private val logger = LoggerFactory.getLogger(XmlReader::class.java)

    override fun readSwiftRecords(): List<SwiftRecord> {
        val records = mutableListOf<SwiftRecord>()
        logger.info("Reading Swift records from XML.")

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(ClassPathResource("/${props.xmlFile}").inputStream)

        document.documentElement.normalize()
        val nodeList = document.getElementsByTagName("record")
        logger.info("Found ${nodeList.length} records in XML.")


        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val element = node as Element

                try {
                    val reference = element.getAttribute("reference")
                    val accountNumber = getContentOrNull(element, "accountNumber")
                    val description = getContentOrNull(element, "description") ?: ""
                    val startBalance = getContentOrNull(element, "startBalance")?.toDoubleOrNull()
                    val mutation = getContentOrNull(element, "mutation")?.toDoubleOrNull()
                    val endBalance = getContentOrNull(element, "endBalance")?.toDoubleOrNull()

                    if (reference.isBlank() || accountNumber == null || startBalance == null || mutation == null || endBalance == null) {
                        logger.warn("Skipping malformed XML record: reference=$reference, accountNumber=$accountNumber, startBalance=$startBalance, mutation=$mutation, endBalance=$endBalance")
                        continue
                    }

                    val swiftRecord = SwiftRecord(
                        reference,
                        accountNumber,
                        description,
                        startBalance,
                        mutation,
                        endBalance
                    )
                    records.add(swiftRecord)

                } catch (ex: Exception) {
                    logger.warn("Skipping malformed XML record due to exception", ex)
                }
            }
        }
        logger.info("Successfully read ${records.size} valid records from XML.")
        return records
    }

    private fun getContentOrNull(element: Element, tagName: String): String? {
        val nodeList = element.getElementsByTagName(tagName)
        val node = nodeList.item(0) ?: return null
        return node.textContent
    }
}
