package com.nuzhnov.workcontrol.core.session.domen.model

import com.nuzhnov.workcontrol.core.model.Discipline
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Teacher

sealed interface UserData {
    data class TeacherData(
        val teacher: Teacher,
        val disciplineList: List<Discipline>
    ) : UserData

    data class StudentData(val student: Student) : UserData
}
