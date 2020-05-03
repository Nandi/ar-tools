package com.headlessideas

import org.jgrapht.Graph
import org.jgrapht.nio.Attribute
import org.jgrapht.nio.DefaultAttribute
import org.jgrapht.nio.dot.DOTExporter
import org.jgrapht.nio.json.JSONExporter
import java.io.File
import java.io.FileOutputStream

fun <E : Any> Graph<String, E>.toDotFile(root: File, fileName: String = "graph.dot") {
    val exporter = DOTExporter<String, E>()
    exporter.setVertexAttributeProvider {
        attributeMap(root, it)
    }
    val output = FileOutputStream(fileName)
    exporter.exportGraph(this, output)
}

fun <E : Any> Graph<String, E>.toJsonFile(root: File, fileName: String = "graph.json") {
    val exporter = JSONExporter<String, E>()
    exporter.setVertexAttributeProvider {
        attributeMap(root, it)
    }
    val output = FileOutputStream(fileName)
    exporter.exportGraph(this, output)
}

private fun attributeMap(root: File, module: String): Map<String, Attribute> {
    return mapOf(
        "label" to DefaultAttribute.createAttribute(module.replace("io.ktor.", "")),
        "size" to DefaultAttribute.createAttribute(loc(root, module))
    )
}