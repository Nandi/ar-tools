package com.headlessideas

import java.io.File
import java.nio.file.Files

fun File.findAllKotlinFiles(): List<File> = findAllFilesWithExtension(listOf("kt", "kts"))
fun File.findAllJavaFiles(): List<File> = findAllFilesWithExtension(listOf("java"))


fun File.findAllFilesWithExtension(extensions: List<String>): List<File> {
    return walk()
        .filter { it.isFile && it.extension in extensions }
        .toList()
}

fun File.findSrcFolders(): List<File> {
    return walk()
        .filter { it.isDirectory && it.name == "src" }
        .toList()
}

fun File.srcOrRoot():File {
    return findSrcFolders().firstOrNull() ?: this
}

//Ktor directory layout is /<module name>/jvm/src
fun File.getTopModules(): List<File> {
    return Files.newDirectoryStream(toPath())
        .map { it.toFile() }
        .filter { it.findSrcFolders().isNotEmpty() }
}