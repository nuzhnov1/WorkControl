package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.LessonGroupCrossRefEntity
import com.nuzhnov.workcontrol.core.data.preferences.AppPreferences
import com.nuzhnov.workcontrol.core.data.mapper.toDateTimeTz
import com.nuzhnov.workcontrol.core.data.mapper.toDouble
import com.nuzhnov.workcontrol.core.data.mapper.toLong
import com.nuzhnov.workcontrol.core.model.Lesson
import com.nuzhnov.workcontrol.core.model.Role
import com.nuzhnov.workcontrol.core.util.coroutines.util.safeExecute
import com.nuzhnov.workcontrol.core.util.coroutines.di.annotation.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import com.soywiz.klock.DateTimeTz

internal class LessonLocalDataSource @Inject constructor(
    private val disciplineDAO: DisciplineDAO,
    private val buildingDAO: BuildingDAO,
    private val roomDAO: RoomDAO,
    private val departmentDAO: DepartmentDAO,
    private val groupDAO: GroupDAO,
    private val teacherDAO: TeacherDAO,
    private val lessonDAO: LessonDAO,
    private val lessonGroupCrossRefDAO: LessonGroupCrossRefDAO,
    private val appPreferences: AppPreferences,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    fun getTeacherCreatedLessonsFlow(): Flow<List<LessonModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                lessonDAO
                    .getTeacherCreatedLessonsFlow(teacherID = session.id)
                    .flowOn(context = coroutineDispatcher)
                    .collect { lessonModelList -> emit(lessonModelList) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }

    fun getTeacherActiveLessonFlow(): Flow<LessonModel?> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                lessonDAO
                    .getTeacherActiveLessonFlow(teacherID = session.id)
                    .flowOn(context = coroutineDispatcher)
                    .collect { lessonModel -> emit(lessonModel) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }

    fun getTeacherFinishedLessonsFlow(): Flow<List<LessonModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                lessonDAO
                    .getTeacherFinishedLessonFlow(teacherID = session.id)
                    .flowOn(context = coroutineDispatcher)
                    .collect { lessonModelList -> emit(lessonModelList) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }

    fun getTeacherDisciplineCreatedLessonsFlow(
        disciplineID: Long
    ): Flow<List<LessonModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                lessonDAO
                    .getTeacherCreatedDisciplineLessonsFlow(teacherID = session.id, disciplineID)
                    .flowOn(context = coroutineDispatcher)
                    .collect { lessonModelList -> emit(lessonModelList) }
            } else {
                throw IllegalStateException("permission denied")
            }
        }

    fun getTeacherDisciplineFinishedLessonsFlow(
        disciplineID: Long
    ): Flow<List<LessonModel>> = appPreferences
        .getSessionFlow()
        .transform { session ->
            if (session?.role == Role.TEACHER) {
                lessonDAO
                    .getTeacherFinishedDisciplineLessonsFlow(teacherID = session.id, disciplineID)
                    .flowOn(context = coroutineDispatcher)
                    .collect { lessonModelList -> emit(lessonModelList) }
            }
        }

    suspend fun saveLessonModels(vararg lessonModel: LessonModel): Result<Unit> = safeExecute {
        val lessonEntityArray = lessonModel
            .map { lessonModel -> lessonModel.lessonEntity }
            .toTypedArray()

        val disciplineEntityArray = lessonModel
            .map { lessonModel -> lessonModel.disciplineEntity }
            .distinct()
            .toTypedArray()

        val teacherEntityArray = lessonModel
            .map { lessonModel -> lessonModel.teacherEntity }
            .distinct()
            .toTypedArray()

        val roomModelList = lessonModel
            .map { lessonModel -> lessonModel.roomModel }
            .distinct()

        val roomEntityArray = roomModelList
            .map { roomModel -> roomModel.roomEntity }
            .toTypedArray()

        val buildingEntityArray = roomModelList
            .map { roomModel -> roomModel.buildingEntity }
            .distinct()
            .toTypedArray()

        val groupModelList = lessonModel
            .map { lessonModel -> lessonModel.groupModelList }
            .flatten()
            .distinct()

        val groupEntityArray = groupModelList
            .map { groupModel -> groupModel.groupEntity }
            .toTypedArray()

        val departmentEntityArray = groupModelList
            .map { groupModel -> groupModel.departmentEntity }
            .distinct()
            .toTypedArray()

        val lessonGroupCrossRefEntityArray = lessonModel
            .map { lessonModel ->
                lessonModel
                    .groupModelList
                    .map { groupModel -> groupModel.groupEntity.id }
                    .map { groupID ->
                        LessonGroupCrossRefEntity(
                            lessonID = lessonModel.lessonEntity.id,
                            groupID = groupID
                        )
                    }
            }
            .flatten()
            .toTypedArray()

        disciplineDAO.insertOrUpdate(*disciplineEntityArray)
        teacherDAO.insertOrUpdate(*teacherEntityArray)
        buildingDAO.insertOrUpdate(*buildingEntityArray)
        roomDAO.insertOrUpdate(*roomEntityArray)
        departmentDAO.insertOrUpdate(*departmentEntityArray)
        groupDAO.insertOrUpdate(*groupEntityArray)
        lessonDAO.insertOrUpdate(*lessonEntityArray)
        lessonGroupCrossRefDAO.insertOrUpdate(*lessonGroupCrossRefEntityArray)
    }

    suspend fun addLesson(lessonModel: LessonModel): Result<Unit> = saveLessonModels(lessonModel)

    suspend fun updateLesson(lessonModel: LessonModel): Result<Unit> = safeExecute {
        removeLesson(lessonModel.lessonEntity).getOrThrow()
        addLesson(lessonModel).getOrThrow()
    }

    suspend fun removeLesson(lessonEntity: LessonEntity): Result<Unit> = safeExecute {
        lessonDAO.delete(lessonEntity)
    }

    suspend fun startLesson(lessonID: Long): Result<Unit> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role != Role.TEACHER) {
            throw IllegalStateException("permission denied")
        }

        val activeLesson = lessonDAO.getTeacherActiveLesson(teacherID = session.id)

        if (activeLesson != null) {
            throw IllegalStateException("one of the lessons has already active")
        } else {
            val lessonEntity = lessonDAO.getEntity(lessonID)
                ?: throw IllegalStateException("lesson not found")

            val startedLesson = lessonEntity.copy(
                startTime = DateTimeTz.nowLocal().toLong(),
                state = Lesson.State.ACTIVE,
                isSynchronised = false
            )

            lessonDAO.insertOrUpdate(startedLesson)
        }
    }

    suspend fun finishLesson(lessonID: Long): Result<Unit> = safeExecute {
        val session = appPreferences.getSession()

        if (session?.role != Role.TEACHER) {
            throw IllegalStateException("permission denied")
        }

        val lessonEntity = lessonDAO.getEntity(lessonID)
            ?: throw IllegalStateException("the lesson is not found")

        val nowTime = DateTimeTz.nowLocal()
        val startTime = lessonEntity.startTime
            ?.toDateTimeTz()
            ?: throw IllegalStateException("the lesson start time is not specified")

        val finishedLesson = lessonEntity.copy(
            actualDuration = (nowTime - startTime).toDouble(),
            state = Lesson.State.FINISHED,
            isSynchronised = false
        )

        lessonDAO.insertOrUpdate(finishedLesson)
    }
}
