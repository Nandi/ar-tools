package com.headlessideas

import java.io.File

fun File.findPackageName(): String {
    return readLines()
        .first { it.startsWith("package") }
        .replace("package ", "")
}

fun File.imports(): List<String> {
    return readLines()
        .filter { it.startsWith("import") }
        .map { it.replace("import ", "").replace(".*", "") }
}

fun moduleWhitelist(module: String): Boolean {
    return module.startsWith("io.ktor")
}

fun abstractModuleToLevel(module: String, depth: Int = 1): String {
    return module.split(".")
        .take(depth)
        .joinToString(".")
}

fun File.loc(): Int {
    return readLines().size
}

fun loc(root: File, module: String): Int {
    val loc = root.srcOrRoot()
        .findAllKotlinFiles()
        .filter { f -> f.findPackageName().startsWith(module) }
        .sumBy { it.loc() }

    if (loc == 0) println(module)

    return loc
}