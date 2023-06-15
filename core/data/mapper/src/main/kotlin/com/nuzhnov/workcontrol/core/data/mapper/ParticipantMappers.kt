package com.nuzhnov.workcontrol.core.data.mapper

import com.nuzhnov.workcontrol.core.models.Participant
import com.nuzhnov.workcontrol.core.models.Lesson
import com.nuzhnov.workcontrol.core.models.Student
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.ParticipantLessonModelDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.NewParticipantDTO
import com.nuzhnov.workcontrol.core.data.api.dto.lesson.UpdatedParticipantDTO
import com.nuzhnov.workcontrol.core.data.database.entity.ParticipantEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.ParticipantLessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.model.update.ParticipantUpdatableModel


fun ParticipantModelDTO.toParticipantEntity(lessonID: Long) = ParticipantEntity(
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

fun ParticipantEntity.toNewParticipantDTO() = NewParticipantDTO(
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

fun ParticipantEntity.toUpdatedParticipantDTO() = UpdatedParticipantDTO(
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

fun ParticipantModel.toParticipant(lesson: Lesson) = Participant(
    student = studentModel.toStudent(),
    lesson = lesson,
    isActive = participantEntity.isActive,
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

fun ParticipantLessonModel.toParticipant(student: Student) = Participant(
    student = student,
    lesson = lessonModel.toLesson(),
    isActive = participantEntity.isActive,
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

fun Participant.toParticipantUpdatableModel() = ParticipantUpdatableModel(
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
