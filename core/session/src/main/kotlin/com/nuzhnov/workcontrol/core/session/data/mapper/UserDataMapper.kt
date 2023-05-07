package com.nuzhnov.workcontrol.core.session.data.mapper

import com.nuzhnov.workcontrol.core.session.domen.model.UserData
import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.*
import com.nuzhnov.workcontrol.core.database.entity.*
import com.nuzhnov.workcontrol.core.database.entity.model.*
import com.nuzhnov.workcontrol.core.model.*


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

internal fun Discipline.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(
    id = id,
    name = name
)

internal fun Faculty.toFacultyEntity(): FacultyEntity = FacultyEntity(
    id = id,
    name = name
)

internal fun Group.toGroupEntity(): GroupEntity = GroupEntity(
    id = id,
    name = name,
    course = course,
    facultyID = faculty.id
)

internal fun Student.toStudentEntity(): StudentEntity = StudentEntity(
    id = id,
    name = name,
    email = email,
    groupID = group.id
)

internal fun Teacher.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = id,
    name = name,
    email = email
)

private fun DisciplineDTO.toDiscipline(): Discipline = Discipline(
    id = id,
    name = name
)

private fun FacultyDTO.toFaculty(): Faculty = Faculty(
    id = id,
    name = name
)

private fun GroupModelDTO.toGroup(): Group = Group(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    faculty = facultyDTO.toFaculty()
)

private fun StudentModelDTO.toStudent(): Student = Student(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    group = groupModelDTO.toGroup()
)

private fun TeacherDTO.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)

private fun DisciplineEntity.toDiscipline(): Discipline = Discipline(
    id = id,
    name = name
)

private fun FacultyEntity.toFaculty(): Faculty = Faculty(
    id = id,
    name = name
)

private fun GroupModel.toGroup(): Group = Group(
    id = groupEntity.id,
    name = groupEntity.name,
    course = groupEntity.course,
    faculty = facultyEntity.toFaculty()
)

private fun StudentModel.toStudent(): Student = Student(
    id = studentEntity.id,
    name = studentEntity.name,
    email = studentEntity.email,
    group = groupModel.toGroup()
)

private fun TeacherEntity.toTeacher(): Teacher = Teacher(
    id = id,
    name = name,
    email = email
)
