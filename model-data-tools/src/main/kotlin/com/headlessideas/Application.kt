package com.headlessideas

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {
    private val codeRoot: File by argument(help = "Root folder to scan")
        .file()

    override fun run() {
        val topModules = codeRoot.getTopModules()
        topModules.forEach { file ->
            println(file)
            val graph = buildGraph(file)
            graph.toDotFile(file, "${file.name}.dot")
        }
    }
}

fun printFileStats(file: File) {
    val kotlinFiles = file.findAllKotlinFiles()
    println("------------- $file -------------")
    println("------------- All Kotlin files (total: ${kotlinFiles.size}) -------------")
}