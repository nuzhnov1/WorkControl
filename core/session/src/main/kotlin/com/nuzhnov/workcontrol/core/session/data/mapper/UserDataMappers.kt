package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.data.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.data.database.entity.DisciplineEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.StudentModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.TeacherModel
import com.nuzhnov.workcontrol.core.data.mapper.toDiscipline
import com.nuzhnov.workcontrol.core.data.mapper.toStudent
import com.nuzhnov.workcontrol.core.data.mapper.toTeacher


internal fun StudentModelDTO.toUserData() = UserData.StudentData(student = this.toStudent())

internal fun TeacherModelDTO.toUserData() = UserData.TeacherData(
    teacher = teacherDTO.toTeacher(),
    disciplineList = disciplineDTOList.map(DisciplineDTO::toDiscipline)
)

internal fun StudentModel.toUserData() = UserData.StudentData(student = this.toStudent())

internal fun TeacherModel.toUserData() = UserData.TeacherData(
    teacher = teacherEntity.toTeacher(),
    disciplineList = disciplineEntityList.map(DisciplineEntity::toDiscipline)
)
