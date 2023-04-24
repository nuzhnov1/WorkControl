package com.nuzhnov.workcontrol.shared.resources

import android.content.Context


val Context.lectureLessonType get() = getString(R.string.lecture_lesson_type)
val Context.practiceLessonType get() = getString(R.string.practice_lesson_type)

val Context.intramuralVisitType get() = getString(R.string.intramural_visit_type)
val Context.remoteVisitType get() = getString(R.string.remote_visit_type)
val Context.mixVisitType get() = getString(R.string.mix_visit_type)
