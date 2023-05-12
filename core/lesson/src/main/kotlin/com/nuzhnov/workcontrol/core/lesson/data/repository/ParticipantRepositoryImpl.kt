package com.nuzhnov.workcontrol.core.lesson.data.repository

import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantRemoteDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.ParticipantLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.LessonLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.data.datasource.StudentLocalDataSource
import com.nuzhnov.workcontrol.core.lesson.domen.repository.ParticipantRepository
import com.nuzhnov.workcontrol.core.api.dto.lesson.ParticipantModelDTO
import com.nuzhnov.workcontrol.core.api.dto.lesson.ParticipantLessonModelDTO
import com.nuzhnov.workcontrol.core.api.util.Response
import com.nuzhnov.workcontrol.core.mapper.*
import com.nuzhnov.workcontrol.core.model.Participant
import com.nuzhnov.workcontrol.core.model.Student
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.util.LoadResult
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ParticipantRepositoryImpl @Inject constructor(
    private val participantRemoteDataSource: ParticipantRemoteDataSource,
    private val participantLocalDataSource: ParticipantLocalDataSource,
    private val lessonLocalDataSource: LessonLocalDataSource,
    private val studentLocalDataSource: StudentLocalDataSource
) : ParticipantRepository {

    override fun getParticipantsOfLessonFlow(
        lesson: Lesson
    ): Flow<LoadResult<List<Participant>>> = when (lesson.state) {
        Lesson.State.CREATED, Lesson.State.ACTIVE -> participantLocalDataSource
            .getParticipantsOfTeacherLessonFlow(lessonID = lesson.id)
            .map { participantModelList ->
                LoadResult.Success(
                    data = participantModelList.map { participantModel ->
                        participantModel.toParticipant(lesson)
                    }
                )
            }

        Lesson.State.FINISHED -> participantLocalDataSource
            .getParticipantsOfTeacherLessonFlow(lessonID = lesson.id)
            .map { participantModelList ->
                if (participantModelList.isEmpty()) {
                    loadParticipantsOfFinishedLesson(lesson)
                } else {
                    LoadResult.Success(
                        data = participantModelList.map { participantModel ->
                            participantModel.toParticipant(lesson)
                        }
                    )
                }
            }
    }

    override fun getStudentParticipationOfTeacherLessonsFlow(
        student: Student
    ): Flow<LoadResult<List<Participant>>> = participantLocalDataSource
        .getStudentParticipationOfTeacherLessonsFlow(studentID = student.id)
        .map { participantLessonModelList ->
            if (participantLessonModelList.isEmpty()) {
                loadStudentParticipationOfTeacherLessons(student)
            } else {
                LoadResult.Success(
                    data = participantLessonModelList.map { participantLessonModel ->
                        participantLessonModel.toParticipant(student)
                    }
                )
            }
        }

    override fun getStudentParticipationOfLessonsFlow(): Flow<LoadResult<List<Participant>>> =
        participantLocalDataSource
            .getStudentParticipationOfLessonsFlow()
            .map { (student, participantLessonModelList) ->
                if (participantLessonModelList.isEmpty()) {
                    loadStudentParticipationOfLessons()
                } else {
                    LoadResult.Success(
                        data = participantLessonModelList.map { participantLessonModel ->
                            participantLessonModel.toParticipant(student)
                        }
                    )
                }
            }

    override suspend fun loadParticipantsOfFinishedLesson(
        lesson: Lesson
    ): LoadResult<List<Participant>> = safeExecute {
        val lessonID = lesson.id
        val response = participantRemoteDataSource.getParticipantsOfFinishedTeacherLesson(lessonID)

        if (response is Response.Success<List<ParticipantModelDTO>>) {
            val participantModelDTOList = response.value

            val participantEntityArray = participantModelDTOList
                .map { participantModelDTO -> participantModelDTO.toParticipantEntity(lessonID) }
                .toTypedArray()

            val studentEntityArray = participantModelDTOList
                .map { participantModelDTO -> participantModelDTO.studentModelDTO.toStudentEntity() }
                .toTypedArray()

            studentLocalDataSource
                .saveStudentEntities(*studentEntityArray)
                .getOrThrow()

            participantLocalDataSource
                .saveParticipantEntities(*participantEntityArray)
                .getOrThrow()
        }

        response.toLoadResult { participantModelDTOList ->
            participantModelDTOList.map { participantModelDTO ->
                participantModelDTO.toParticipant(lesson)
            }
        }
    }.unwrap()

    override suspend fun loadStudentParticipationOfTeacherLessons(
        student: Student
    ): LoadResult<List<Participant>> = safeExecute {
        val studentID = student.id
        val response = participantRemoteDataSource
            .getStudentParticipationOfFinishedTeacherLessons(studentID)

        if (response is Response.Success<List<ParticipantLessonModelDTO>>) {
            val participantLessonModelDTOList = response.value

            val participantEntityArray = participantLessonModelDTOList
                .map { participantLessonModelDTO ->
                    participantLessonModelDTO.toParticipantEntity(studentID)
                }
                .toTypedArray()

            val lessonModelArray = participantLessonModelDTOList
                .map { participantLessonModelDTO ->
                    participantLessonModelDTO.lessonDTO.toLessonModel()
                }
                .toTypedArray()

            lessonLocalDataSource
                .saveLessonModels(*lessonModelArray)
                .getOrThrow()

            participantLocalDataSource
                .saveParticipantEntities(*participantEntityArray)
                .getOrThrow()
        }

        response.toLoadResult { participantLessonModelDTOList ->
            participantLessonModelDTOList.map { participantLessonModelDTO ->
                participantLessonModelDTO.toParticipant(student)
            }
        }
    }.unwrap()

    override suspend fun loadStudentParticipationOfLessons(): LoadResult<List<Participant>> =
        safeExecute {
            val student = studentLocalDataSource
                .getCurrentStudentModel()
                .getOrThrow()
                .toStudent()

            val studentID = student.id
            val response = participantRemoteDataSource.getStudentParticipationOfFinishedLessons()

            if (response is Response.Success<List<ParticipantLessonModelDTO>>) {
                val participantLessonModelDTOList = response.value

                val participantEntityArray = participantLessonModelDTOList
                    .map { participantLessonModelDTO ->
                        participantLessonModelDTO.toParticipantEntity(studentID)
                    }
                    .toTypedArray()

                val lessonModelArray = participantLessonModelDTOList
                    .map { participantLessonModelDTO ->
                        participantLessonModelDTO.lessonDTO.toLessonModel()
                    }
                    .toTypedArray()

                lessonLocalDataSource
                    .saveLessonModels(*lessonModelArray)
                    .getOrThrow()

                participantLocalDataSource
                    .saveParticipantEntities(*participantEntityArray)
                    .getOrThrow()
            }

            response.toLoadResult { participantLessonModelDTOList ->
                participantLessonModelDTOList.map { participantLessonModelDTO ->
                    participantLessonModelDTO.toParticipant(student)
                }
            }
        }.unwrap()

    override suspend fun updateParticipant(participant: Participant): Result<Unit> =
        participantLocalDataSource.updateParticipant(
            participantUpdatableModel = participant.toParticipantUpdatableModel()
        )
}
