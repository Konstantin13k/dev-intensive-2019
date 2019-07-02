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
    var isOnline: Boolean = false
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

        private fun buildUser(
            id: String? = null,
            firstName: String?,
            lastName: String?,
            avatar: String?,
            rating: Int,
            respect: Int,
            lastVisit: Date?,
            isOnline: Boolean
        ): User {
            return User(
                id = "${if (id.isNullOrEmpty()) ++lastid else id}",
                firstName = firstName,
                lastName = lastName,
                avatar = avatar,
                rating = rating,
                respect = respect,
                lastVisit = lastVisit,
                isOnline = isOnline
            )
        }
    }

    data class Builder(
        var id: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var avatar: String? = null,
        var rating: Int = 0,
        var respect: Int = 0,
        var lastVisit: Date? = null,
        var isOnline: Boolean = false
    ) {
        fun id(id: String) = apply { this.id = id }
        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun avatar(avatar: String) = apply { this.avatar = avatar }
        fun rating(rating: Int) = apply { this.rating = rating }
        fun respect(respect: Int) = apply { this.respect = respect }
        fun lastVisit(lastVisit: Date) = apply { this.lastVisit = lastVisit }
        fun isOnline(isOnline: Boolean) = apply { this.isOnline = isOnline }
        fun build() = User.buildUser(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
    }
}