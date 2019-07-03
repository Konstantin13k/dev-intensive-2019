package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60L
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Long, units: TimeUnits = TimeUnits.SECOND): Date {
    val multiplier = value * SECOND
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> multiplier
        TimeUnits.MINUTE -> multiplier * MINUTE
        TimeUnits.HOUR -> multiplier * HOUR
        TimeUnits.DAY -> multiplier * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = this): String {
    val timeDiff = (Date().time - date.time) / SECOND
    return if (timeDiff >= 0) pastHumanizeDiff(timeDiff) else futureHumanizeDiff(timeDiff)
}

private fun pastHumanizeDiff(timeDiff: Long): String {
    return when (timeDiff) {
        0L, 1L -> "только что"
        in 1..45 -> "несколько секунд назад"
        in 45..75 -> "минуту назад"
        in 75..45 * MINUTE -> "${diff(timeDiff / MINUTE, TimeUnits.MINUTE)} назад"
        in 45 * MINUTE..75 * MINUTE -> "час назад"
        in 75 * MINUTE..22 * HOUR -> "${diff(timeDiff / HOUR, TimeUnits.HOUR)} назад"
        in 22 * HOUR..26 * HOUR -> "день назад"
        in 26 * HOUR..360 * DAY -> "${diff(timeDiff / DAY, TimeUnits.DAY)} назад"
        else -> "более года назад"
    }
}

private fun futureHumanizeDiff(timeDiff: Long): String {
    return when (-timeDiff) {
        0L, 1L -> "через несколько секунд"
        in 1..45 -> "через несколько секунд"
        in 45..75 -> "через минуту"
        in 75..45 * MINUTE -> "через ${diff(-timeDiff / MINUTE, TimeUnits.MINUTE)}"
        in 45 * MINUTE..75 * MINUTE -> "через час"
        in 75 * MINUTE..22 * HOUR -> "через ${diff(-timeDiff / HOUR, TimeUnits.HOUR)}"
        in 22 * HOUR..26 * HOUR -> "через день"
        in 26 * HOUR..360 * DAY -> "через ${diff(-timeDiff / DAY, TimeUnits.DAY)}"
        else -> "более чем через год"
    }
}

private fun diff(timeDiff: Long, units: TimeUnits): String {
    return "$timeDiff ${when (if (timeDiff > 20) timeDiff % 10 else timeDiff) {
        1L -> if (units == TimeUnits.MINUTE) "минуту" else if (units == TimeUnits.HOUR) "час" else "день"
        in 2..4 -> if (units == TimeUnits.MINUTE) "минуты" else if (units == TimeUnits.HOUR) "часа" else "дня"
        else -> if (units == TimeUnits.MINUTE) "минут" else if (units == TimeUnits.HOUR) "часов" else "дней"
    }}"
}

enum class TimeUnits {
    SECOND {
        override fun plural(value: Int) = "$value ${when (if (value > 20) value % 10 else value) {
            1 -> "секунду"
            in 2..4 -> "секунды"
            else -> "секунд"
        }
        }"
    },
    MINUTE {
        override fun plural(value: Int) = "$value ${when (if (value > 20) value % 10 else value) {
            1 -> "минуту"
            in 2..4 -> "минуты"
            else -> "минут"
        }
        }"
    },
    HOUR {
        override fun plural(value: Int) = "$value ${when (if (value > 20) value % 10 else value) {
            1 -> "час"
            in 2..4 -> "часа"
            else -> "часов"
        }
        }"
    },
    DAY {
        override fun plural(value: Int) = "$value ${when (if (value > 20) value % 10 else value) {
            1 -> "день"
            in 2..4 -> "дня"
            else -> "дней"
        }
        }"
    };

    abstract fun plural(value: Int): String
}