package com.nuzhnov.workcontrol.shared.resources

import com.nuzhnov.workcontrol.shared.models.Lesson
import android.content.Context


fun Lesson.Type.toResourceString(context: Context): String = when (this) {
    Lesson.Type.LECTURE -> context.getString(R.string.lecture_lesson_type)
    Lesson.Type.PRACTICE -> context.getString(R.string.practice_lesson_type)
}

fun Lesson.VisitType.toResourceString(context: Context): String = when (this) {
    Lesson.VisitType.INTRAMURAL -> context.getString(R.string.intramural_visit_type)
    Lesson.VisitType.REMOTE -> context.getString(R.string.remote_visit_type)
    Lesson.VisitType.MIX -> context.getString(R.string.mix_visit_type)
}
