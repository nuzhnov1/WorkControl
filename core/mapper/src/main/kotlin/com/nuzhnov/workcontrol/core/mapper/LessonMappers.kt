package com.nuzhnov.workcontrol.core.mapper

import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Group
import com.nuzhnov.workcontrol.core.api.dto.lesson.LessonDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.NewLessonDTO
import com.nuzhnov.workcontrol.core.api.dto.university.GroupModelDTO
import com.nuzhnov.workcontrol.core.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.database.entity.model.GroupModel
import com.nuzhnov.workcontrol.core.database.entity.ParticipantEntity


fun LessonDTO.toLesson(): Lesson = Lesson(
    id = id,
    discipline = disciplineDTO.toDiscipline(),
    teacher = teacherDTO.toTeacher(),
    room = roomModelDTO.toRoom(),
    associatedGroups = groupModelDTOList.map(GroupModelDTO::toGroup),
    theme = theme,
    type = type,
    startTime = startTime?.toDateTimeTz(),
    plannedDuration = plannedDuration.toTimeSpan(),
    actualDuration = actualDuration?.toTimeSpan(),
    state = Lesson.State.FINISHED
)

fun LessonDTO.toLessonEntity(): LessonEntity = LessonEntity(
    id = id,
    disciplineID = disciplineDTO.id,
    teacherID = teacherDTO.id,
    roomID = roomModelDTO.roomDTO.id,
    theme = theme,
    type = type,
    startTime = startTime,
    plannedDuration = plannedDuration,
    actualDuration = actualDuration,
    state = Lesson.State.FINISHED,
    isSynchronised = true
)

fun LessonDTO.toLessonModel(): LessonModel = LessonModel(
    lessonEntity = this.toLessonEntity(),
    groupModelList = groupModelDTOList.map(GroupModelDTO::toGroupModel),
    teacherEntity = teacherDTO.toTeacherEntity(),
    disciplineEntity = disciplineDTO.toDisciplineEntity(),
    roomModel = roomModelDTO.toRoomModel()
)

fun LessonModel.toLesson(): Lesson = Lesson(
    id = lessonEntity.id,
    discipline = disciplineEntity.toDiscipline(),
    teacher = teacherEntity.toTeacher(),
    room = roomModel.toRoom(),
    associatedGroups = groupModelList.map(GroupModel::toGroup),
    theme = lessonEntity.theme,
    type = lessonEntity.type,
    startTime = lessonEntity.startTime?.toDateTimeTz(),
    plannedDuration = lessonEntity.plannedDuration.toTimeSpan(),
    actualDuration = lessonEntity.actualDuration?.toTimeSpan(),
    state = lessonEntity.state
)

fun Lesson.toLessonEntity(): LessonEntity = LessonEntity(
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

fun Lesson.toLessonModel(): LessonModel = LessonModel(
    lessonEntity = this.toLessonEntity(),
    groupModelList = associatedGroups.map(Group::toGroupModel),
    teacherEntity = teacher.toTeacherEntity(),
    disciplineEntity = discipline.toDisciplineEntity(),
    roomModel = room.toRoomModel()
)

fun Pair<LessonEntity, Iterable<ParticipantEntity>>.toNewLessonDTO(): NewLessonDTO =
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