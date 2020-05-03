package com.headlessideas

//todo: Use this to as vertex?
data class SourceCode(
    val module: String,
    val imports: List<String>,
    val loc: Int
)