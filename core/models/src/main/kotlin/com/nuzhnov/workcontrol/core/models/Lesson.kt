package com.nuzhnov.workcontrol.core.models

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.hours

data class Lesson(
    val id: Long,
    val discipline: Discipline,
    val teacher: Teacher,
    val room: Room,
    val theme: String,
    val visitType: VisitType,
    val startTime: DateTimeTz? = null,
    val plannedDuration: TimeSpan = DEFAULT_PLANNED_DURATION,
    val actualDuration: TimeSpan = TimeSpan.ZERO,
    val state: State = State.CREATED
) {

    init {
        checkConstraints()
    }

    private fun checkConstraints() {
        checkThemeConstraints()
    }

    private fun checkThemeConstraints() {
        if (theme.length !in THEME_LENGTH_RANGE) {
            throw IllegalArgumentException(
                "the length of the theme isn't in the range " +
                "[${THEME_LENGTH_RANGE.first}:${THEME_LENGTH_RANGE.last}]"
            )
        }
    }


    enum class VisitType {
        INTRAMURAL,
        REMOTE,
        MIX
    }

    enum class State {
        CREATED,
        STARTED,
        FINISHED
    }

    companion object {
        val THEME_LENGTH_RANGE = 1..150
        val DEFAULT_PLANNED_DURATION = 1.5.hours
    }
}
