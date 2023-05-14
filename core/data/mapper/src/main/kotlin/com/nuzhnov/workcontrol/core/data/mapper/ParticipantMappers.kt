package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.model.Participant
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantLessonModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.NewParticipantDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.UpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantLessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantUpdatableModel


fun ParticipantModelDTO.toParticipant(lesson: Lesson): Participant = Participant(
    student = studentModelDTO.toStudent(),
    lesson = lesson,
    totalVisitDuration = totalVisitDuration.toTimeSpan(),
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note
)

fun ParticipantModelDTO.toParticipantEntity(lessonID: Long): ParticipantEntity = ParticipantEntity(
    studentID = studentModelDTO.studentDTO.id,
    lessonID = lessonID,
    isActive = false,
    lastVisit = null,
    totalVisitDuration = totalVisitDuration,
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note,
    isSynchronized = true
)

fun ParticipantLessonModelDTO.toParticipant(student: Student): Participant = Participant(
    student = student,
    lesson = lessonDTO.toLesson(),
    totalVisitDuration = totalVisitDuration.toTimeSpan(),
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note
)

fun ParticipantLessonModelDTO.toParticipantEntity(studentID: Long) = ParticipantEntity(
    studentID = studentID,
    lessonID = lessonDTO.id,
    isActive = false,
    lastVisit = null,
    totalVisitDuration = totalVisitDuration,
    isMarked = isMarked,
    theoryAssessment = theoryAssessment,
    practiceAssessment = practiceAssessment,
    activityAssessment = activityAssessment,
    prudenceAssessment = prudenceAssessment,
    creativityAssessment = creativityAssessment,
    preparationAssessment = preparationAssessment,
    note = note,
    isSynchronized = true
)

fun ParticipantEntity.toNewParticipantDTO(): NewParticipantDTO = NewParticipantDTO(
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

fun ParticipantEntity.toUpdatedParticipantDTO(): UpdatedParticipantDTO = UpdatedParticipantDTO(
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

fun ParticipantModel.toParticipant(lesson: Lesson): Participant = Participant(
    student = studentModel.toStudent(),
    lesson = lesson,
    totalVisitDuration = participantEntity.totalVisitDuration.toTimeSpan(),
    isMarked = participantEntity.isMarked,
    theoryAssessment = participantEntity.theoryAssessment,
    practiceAssessment = participantEntity.practiceAssessment,
    activityAssessment = participantEntity.activityAssessment,
    prudenceAssessment = participantEntity.prudenceAssessment,
    creativityAssessment = participantEntity.creativityAssessment,
    preparationAssessment = participantEntity.preparationAssessment,
    note = participantEntity.note
)

fun ParticipantLessonModel.toParticipant(student: Student): Participant = Participant(
    student = student,
    lesson = lessonModel.toLesson(),
    totalVisitDuration = participantEntity.totalVisitDuration.toTimeSpan(),
    isMarked = participantEntity.isMarked,
    theoryAssessment = participantEntity.theoryAssessment,
    practiceAssessment = participantEntity.practiceAssessment,
    activityAssessment = participantEntity.activityAssessment,
    prudenceAssessment = participantEntity.prudenceAssessment,
    creativityAssessment = participantEntity.creativityAssessment,
    preparationAssessment = participantEntity.preparationAssessment,
    note = participantEntity.note
)

fun Participant.toParticipantUpdatableModel(): ParticipantUpdatableModel =
    ParticipantUpdatableModel(
        studentID = student.id,
        lessonID = lesson.id,
        isMarked = isMarked,
        theoryAssessment = theoryAssessment,
        practiceAssessment = practiceAssessment,
        activityAssessment = activityAssessment,
        prudenceAssessment = prudenceAssessment,
        creativityAssessment = creativityAssessment,
        preparationAssessment = preparationAssessment,
        note = note
    )
