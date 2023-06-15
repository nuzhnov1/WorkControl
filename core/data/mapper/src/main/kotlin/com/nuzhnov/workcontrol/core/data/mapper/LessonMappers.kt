package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Group
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.LessonDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.NewLessonDTO
import com.nuzhnov.workcontrol.core.data.api.dto.university.GroupDTOModel
import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonEntityModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.GroupEntityModel
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity


fun LessonDTO.toLessonEntity() = LessonEntity(
    id = id,
    disciplineID = disciplineDTO.id,
    teacherID = teacherDTO.id,
    roomID = roomDTOModel.roomDTO.id,
    theme = theme,
    type = type,
    startTime = startTime,
    plannedDuration = plannedDuration,
    actualDuration = actualDuration,
    state = Lesson.State.FINISHED,
    isSynchronised = true
)

fun LessonDTO.toLessonEntityModel() = LessonEntityModel(
    lessonEntity = this.toLessonEntity(),
    groupEntityModelList = groupDTOModelList.map(GroupDTOModel::toGroupEntityModel),
    teacherEntity = teacherDTO.toTeacherEntity(),
    disciplineEntity = disciplineDTO.toDisciplineEntity(),
    roomEntityModel = roomDTOModel.toRoomEntityModel()
)

fun LessonEntityModel.toLesson() = Lesson(
    id = lessonEntity.id,
    discipline = disciplineEntity.toDiscipline(),
    teacher = teacherEntity.toTeacher(),
    room = roomEntityModel.toRoom(),
    associatedGroups = groupEntityModelList.map(GroupEntityModel::toGroup),
    theme = lessonEntity.theme,
    type = lessonEntity.type,
    startTime = lessonEntity.startTime?.toDateTimeTz(),
    plannedDuration = lessonEntity.plannedDuration.toTimeSpan(),
    actualDuration = lessonEntity.actualDuration?.toTimeSpan(),
    state = lessonEntity.state
)

fun Lesson.toLessonEntity() = LessonEntity(
    id = id,
    disciplineID = discipline.id,
    teacherID = teacher.id,
    roomID = room.id,
    theme = theme,
    type = type,
    startTime = startTime?.toLong(),
    plannedDuration = plannedDuration.toDouble(),
    actualDuration = actualDuration?.toDouble(),
    state = state,
    isSynchronised = false
)

fun Lesson.toLessonEntityModel() = LessonEntityModel(
    lessonEntity = this.toLessonEntity(),
    groupEntityModelList = associatedGroups.map(Group::toGroupEntityModel),
    teacherEntity = teacher.toTeacherEntity(),
    disciplineEntity = discipline.toDisciplineEntity(),
    roomEntityModel = room.toRoomEntityModel()
)

fun Pair<LessonEntity, Iterable<ParticipantEntity>>.toNewLessonDTO() =
    let { (lessonEntity, associatedParticipants) ->
        NewLessonDTO(
            disciplineID = lessonEntity.disciplineID,
            teacherID = lessonEntity.teacherID,
            roomID = lessonEntity.roomID,
            theme = lessonEntity.theme,
            type = lessonEntity.type,
            startTime = lessonEntity.startTime,
            plannedDuration = lessonEntity.plannedDuration,
            actualDuration = lessonEntity.actualDuration,
            newParticipantDTOList = associatedParticipants
                .map(ParticipantEntity::toNewParticipantDTO)
        )
    }
