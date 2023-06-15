package com.nuzhnov.workcontrol.core.session.domen.model

import com.nuzhnov.workcontrol.core.models.Teacher
import com.nuzhnov.workcontrol.core.models.Discipline
import com.nuzhnov.workcontrol.core.models.Student

sealed interface UserData {
    data class TeacherData(val teacher: Teacher, val disciplineList: List<Discipline>) : UserData

    data class StudentData(val student: Student) : UserData
}
