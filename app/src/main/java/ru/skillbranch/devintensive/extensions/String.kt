package ru.skillbranch.devintensive.extensions

fun String.truncate(index: Int = 16): String =
    if (index > this.trimEnd().length) this.trimEnd() else this.substring(0, index).trimEnd() + "..."


fun String.stripHtml(): String {
    return Regex("<[^>]*>|&").replace(Regex(" +").replace(this, " "), "")
}