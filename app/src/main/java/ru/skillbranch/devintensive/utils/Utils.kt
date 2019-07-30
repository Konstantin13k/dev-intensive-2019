package ru.skillbranch.devintensive.utils

object Utils {
    private val exceptions = listOf(
        "enterprise", "features",
        "topics", "collections", "trending", "events"
        , "marketplace", "pricing", "nonprofit", "customer-stories"
        , "security", "login", "join"
    )

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return converToNull(firstName) to converToNull(lastName)
    }

    private fun converToNull(string: String?): String? = if (string.isNullOrEmpty()) null else string

    fun toInitials(firstName: String?, lastName: String?): String? {
        val fInitial = getFirstChar(firstName) ?: ""
        val lInitial = getFirstChar(lastName) ?: ""
        val initials = "$fInitial$lInitial"
        return if (initials.isEmpty()) null else initials.toUpperCase()
    }

    private fun getFirstChar(string: String?): Char? = if (string.isNullOrEmpty()) null else string.trim().firstOrNull()

    fun transliteration(payload: String, divider: String = " "): String {
        val iterator: CharIterator = payload.toCharArray().iterator()
        val builder: StringBuilder = java.lang.StringBuilder()

        while (iterator.hasNext()) {
            val c = iterator.nextChar()
            val isLowCase = c.isLowerCase()
            val ch: String = when (c.toLowerCase()) {
                'а' -> "a"
                'б' -> "b"
                'в' -> "v"
                'г' -> "g"
                'д' -> "d"
                'е' -> "e"
                'ё' -> "e"
                'ж' -> "zh"
                'з' -> "z"
                'и' -> "i"
                'й' -> "i"
                'к' -> "k"
                'л' -> "l"
                'м' -> "m"
                'н' -> "n"
                'о' -> "o"
                'п' -> "p"
                'р' -> "r"
                'с' -> "s"
                'т' -> "t"
                'у' -> "u"
                'ф' -> "f"
                'х' -> "h"
                'ц' -> "c"
                'ч' -> "ch"
                'ш' -> "sh"
                'щ' -> "sh"
                'Ъ' -> ""
                'ы' -> "i"
                'ь' -> ""
                'э' -> "e"
                'ю' -> "yu"
                'я' -> "ya"
                else -> divider
            }
            if(ch.length == 1){
                builder.append(if (isLowCase) ch else ch.toUpperCase())
            }else {
                val char = ch.first()
                builder.append(if(isLowCase) ch else char.toUpperCase() + ch.substring(1))
            }
        }
        return builder.toString()
    }

    fun validationGitHub(string: String): Boolean = with(string) {
        if (matches("github.com/.*".toRegex())
            || matches("https://github.com/.*".toRegex())
            || matches("https://www.github.com/.*".toRegex())
            || matches("www.github.com/.*".toRegex())
        ) {
            with(split("github.com/".toRegex())[1]) {
                return !exceptions.contains(this) && isNotEmpty() && !matches(".*/.*".toRegex())
            }
        } else false
    }
}