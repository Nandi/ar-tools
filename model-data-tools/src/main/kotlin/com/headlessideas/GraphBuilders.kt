package com.headlessideas

import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleDirectedGraph
import java.io.File

fun buildAbstractedGraph(root: File, depth: Int = 1): Graph<String, DefaultEdge> {
    val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)

    val files = root
        .srcOrRoot()
        .findAllKotlinFiles()

    val nodes = files.map(File::findPackageName)
        .map { abstractModuleToLevel(it, depth) }
        .distinct()

    nodes.forEach { graph.addVertex(it) }

    for (file in files) {
        val imports = file.imports()
        for (import in imports) {
            val source = abstractModuleToLevel(file.findPackageName(), depth)
            val target = abstractModuleToLevel(import, depth)

            if (source != target && moduleWhitelist(source) && moduleWhitelist(target) && nodes.contains(target))
                graph.addEdge(source, target)
        }
    }

    for (file in files) {
        val imports = file.imports().filter(::moduleWhitelist)
        for (import in imports) {


            val source = abstractModuleToLevel(file.findPackageName(), depth)
            val target = abstractModuleToLevel(import, depth)
            if (source == target) continue

            if (!graph.containsVertex(target)) graph.addVertex(target)
            graph.addEdge(source, target)
        }
    }

    return graph
}

fun buildGraph(root: File): Graph<String, DefaultEdge> {
    val graph = SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)

    val files = root.findAllKotlinFiles()

    files.map(File::findPackageName).distinct().forEach { module -> graph.addVertex(module) }

    for (file in files) {
        val imports = file.imports().filter(::moduleWhitelist)
        for (import in imports) {
            if (!graph.containsVertex(import)) graph.addVertex(import)
            graph.addEdge(file.findPackageName(), import)
        }
    }

    return graph
}