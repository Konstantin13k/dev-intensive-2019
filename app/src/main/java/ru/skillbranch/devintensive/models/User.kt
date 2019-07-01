package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = Date(),
    var isonline: Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    init {
        println(
            "it's Alive!!! \n" +
                    "${if (lastName === "Doe") "His name id $firstName $lastName" else "And has name is $firstName $lastName!!!"}\n"
        )
    }

    companion object Factory {
        private var lastid: Int = -1
        fun makeUser(fullName: String?): User {
            lastid++

            val (firstName, lastName) = Utils.parseFullName(fullName)

            return User(id = "$lastid", firstName = firstName, lastName = lastName)
        }
    }
}