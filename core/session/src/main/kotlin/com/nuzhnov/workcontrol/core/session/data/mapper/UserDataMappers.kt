package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity
import com.nuzhnov.workcontrol.core.database.entity.model.StudentModel
import com.nuzhnov.workcontrol.core.database.entity.model.TeacherModel


internal fun StudentModelDTO.toUserData(): UserData = UserData.StudentData(
    student = this.toStudent()
)

internal fun TeacherModelDTO.toUserData(): UserData = UserData.TeacherData(
    teacher = teacherDTO.toTeacher(),
    disciplineList = disciplineDTOList.map(DisciplineDTO::toDiscipline)
)

internal fun StudentModel.toUserData(): UserData = UserData.StudentData(
    student = this.toStudent()
)

internal fun TeacherModel.toUserData(): UserData = UserData.TeacherData(
    teacher = teacherEntity.toTeacher(),
    disciplineList = disciplineEntityList.map(DisciplineEntity::toDiscipline)
)
