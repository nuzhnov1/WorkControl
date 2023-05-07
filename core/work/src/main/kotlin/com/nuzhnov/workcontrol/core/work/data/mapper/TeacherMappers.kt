package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.database.entity.TeacherEntity


internal fun TeacherModelDTO.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = teacherDTO.id,
    name = teacherDTO.name,
    email = teacherDTO.email
)
