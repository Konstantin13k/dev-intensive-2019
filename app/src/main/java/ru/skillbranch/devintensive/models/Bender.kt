package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (valid, error) = question.validation(answer)
        if (!valid) {
            return "$error${question.question}" to status.color
        }
        return if (question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            if (question == Question.IDLE) {
                question.question to status.color
            } else {
                status = status.nextStatus()
                if (status == Status.NORMAL) {
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }
        }
    }


    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validation(text: String?): Pair<Boolean, String> =
                !(text.isNullOrEmpty() || text.first().isLowerCase()) to "Имя должно начинаться с заглавной буквы\n"
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validation(text: String?): Pair<Boolean, String> =
                !(text.isNullOrEmpty() || text.first().isUpperCase()) to "Профессия должна начинаться со строчной буквы\n"
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validation(text: String?): Pair<Boolean, String> =
                !(text.isNullOrEmpty() || text.matches(".*\\d+.*".toRegex())) to "Материал не должен содержать цифр\n"
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validation(text: String?): Pair<Boolean, String> =
                (!text.isNullOrEmpty() && text.matches("-?\\d+(\\.\\d+)?".toRegex())) to "Год моего рождения должен содержать только цифры\n"
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validation(text: String?): Pair<Boolean, String> =
                (!text.isNullOrEmpty() && text.matches("-?\\d+(\\.\\d+)?".toRegex()) && text.length == 7) to "Серийный номер содержит только цифры, и их 7\n"
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validation(text: String?): Pair<Boolean, String> = true to ""
        };

        abstract fun nextQuestion(): Question
        abstract fun validation(text: String?): Pair<Boolean, String>
    }
}