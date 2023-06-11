package com.nuzhnov.workcontrol.core.model

import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.hours

data class Lesson(
    val id: Long,
    val discipline: Discipline,
    val teacher: Teacher,
    val room: Room,
    val associatedGroups: List<Group>,
    val theme: String,
    val type: Type,
    val startTime: DateTimeTz? = null,
    val plannedDuration: TimeSpan = DEFAULT_PLANNED_DURATION,
    val actualDuration: TimeSpan? = null,
    val state: State
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


    enum class Type {
        LECTURE,
        SEMINAR,
        PRACTICE
    }

    enum class State {
        SCHEDULED,
        ACTIVE,
        FINISHED
    }


    companion object {
        val THEME_LENGTH_RANGE = 1..150
        val DEFAULT_PLANNED_DURATION = 1.5.hours
    }
}
