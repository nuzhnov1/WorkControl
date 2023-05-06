package com.nuzhnov.workcontrol.core.work.data.mapper

import com.nuzhnov.workcontrol.core.api.dto.university.BuildingDTO
import com.nuzhnov.workcontrol.core.api.dto.university.RoomModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.FacultyDTO
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.api.dto.university.DisciplineDTO
import com.nuzhnov.workcontrol.core.api.dto.user.StudentModelDTO
import com.nuzhnov.workcontrol.core.api.dto.user.TeacherModelDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.NewLessonDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.NewParticipantDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.UpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.database.entity.BuildingEntity
import com.nuzhnov.workcontrol.core.database.entity.RoomEntity
import com.nuzhnov.workcontrol.core.database.entity.FacultyEntity
import com.nuzhnov.workcontrol.core.database.entity.GroupEntity
import com.nuzhnov.workcontrol.core.database.entity.StudentEntity
import com.nuzhnov.workcontrol.core.database.entity.TeacherEntity
import com.nuzhnov.workcontrol.core.database.entity.DisciplineEntity
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity


internal fun BuildingDTO.toBuildingEntity(): BuildingEntity = BuildingEntity(
    id = id,
    name = name
)

internal fun RoomModelDTO.toRoomEntity(): RoomEntity = RoomEntity(
    id = roomDTO.id,
    name = roomDTO.name,
    buildingID = buildingDTO.id
)

internal fun FacultyDTO.toFacultyEntity(): FacultyEntity = FacultyEntity(
    id = id,
    name = name
)

internal fun GroupModelDTO.toGroupEntity(): GroupEntity = GroupEntity(
    id = groupDTO.id,
    name = groupDTO.name,
    course = groupDTO.course,
    facultyID = facultyDTO.id
)

internal fun StudentModelDTO.toStudentEntity(): StudentEntity = StudentEntity(
    id = studentDTO.id,
    name = studentDTO.name,
    email = studentDTO.email,
    groupID = groupModelDTO.groupDTO.id
)

internal fun TeacherModelDTO.toTeacherEntity(): TeacherEntity = TeacherEntity(
    id = teacherDTO.id,
    name = teacherDTO.name,
    email = teacherDTO.email
)

internal fun DisciplineDTO.toDisciplineEntity(): DisciplineEntity = DisciplineEntity(
    id = id,
    name = name
)

internal fun Pair<LessonEntity, Iterable<ParticipantEntity>>.toNewLessonDTO(): NewLessonDTO =
    let { (lessonEntity, associatedParticipants) ->
        NewLessonDTO(
            disciplineID = lessonEntity.disciplineID,
            teacherID = lessonEntity.teacherID,
            roomID = lessonEntity.roomID,
            theme = lessonEntity.theme,
            visitType = lessonEntity.visitType,
            startTime = lessonEntity.startTime,
            plannedDuration = lessonEntity.plannedDuration,
            actualDuration = lessonEntity.actualDuration,
            newParticipantDTOList = associatedParticipants.map(ParticipantEntity::toNewParticipantDTO)
        )
    }

private fun ParticipantEntity.toNewParticipantDTO(): NewParticipantDTO = NewParticipantDTO(
    studentID = studentID,
    totalVisitDuration = totalVisitDuration,
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note
)

internal fun ParticipantEntity.toUpdatedParticipantDTO(): UpdatedParticipantDTO =
    UpdatedParticipantDTO(
        lessonID = lessonID,
        studentID = studentID,
        totalVisitDuration = totalVisitDuration,
        isMarked = isMarked,
        theoryAssessment = theoryAssessment,
        practiceAssessment = practiceAssessment,
        activityAssessment = activityAssessment,
        prudenceAssessment = prudenceAssessment,
        creativityAssessment = creativityAssessment,
        preparationAssessment = preparationAssessment,
        note = note
    )
