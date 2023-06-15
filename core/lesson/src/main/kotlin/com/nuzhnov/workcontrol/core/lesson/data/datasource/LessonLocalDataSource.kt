package com.nuzhnov.workcontrol.core.lesson.data.datasource

import com.nuzhnov.workcontrol.core.lesson.domen.exception.LessonException
import com.nuzhnov.workcontrol.core.data.database.dao.*
import com.nuzhnov.workcontrol.core.data.database.entity.LessonEntity
import com.nuzhnov.workcontrol.core.data.database.entity.model.LessonModel
import com.nuzhnov.workcontrol.core.data.database.entity.LessonGroupCrossRefEntity
import com.nuzhnov.workcontrol.core.data.mapper.toDateTimeTz
import com.nuzhnov.workcontrol.core.data.mapper.toDouble
import com.nuzhnov.workcontrol.core.data.mapper.toLong
import com.nuzhnov.workcontrol.core.models.Lesson
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
    private val lessonGroupCrossRefDAO: LessonGroupCrossRefDAO
) {

    fun getTeacherScheduledLessonsFlow(teacherID: Long) =
        lessonDAO.getTeacherScheduledLessonsFlow(teacherID)

    fun getTeacherActiveLessonFlow(teacherID: Long) =
        lessonDAO.getTeacherActiveLessonFlow(teacherID)

    fun getTeacherFinishedLessonsFlow(teacherID: Long) =
        lessonDAO.getTeacherFinishedLessonFlow(teacherID)

    fun getTeacherDisciplineScheduledLessonsFlow(teacherID: Long, disciplineID: Long) =
        lessonDAO.getTeacherDisciplineScheduledLessonsFlow(teacherID, disciplineID)

    fun getTeacherDisciplineFinishedLessonsFlow(teacherID: Long, disciplineID: Long) =
        lessonDAO.getTeacherDisciplineFinishedLessonsFlow(teacherID, disciplineID)

    suspend fun saveLessonModels(vararg lessonModel: LessonModel) {
        val lessonEntityArray = lessonModel
            .map { model -> model.lessonEntity }
            .toTypedArray()

        val disciplineEntityArray = lessonModel
            .map { model -> model.disciplineEntity }
            .distinct()
            .toTypedArray()

        val teacherEntityArray = lessonModel
            .map { model -> model.teacherEntity }
            .distinct()
            .toTypedArray()

        val roomModelList = lessonModel.map { model -> model.roomModel }.distinct()
        val roomEntityArray = roomModelList.map { model -> model.roomEntity }.toTypedArray()

        val buildingEntityArray = roomModelList
            .map { model -> model.buildingEntity }
            .distinct()
            .toTypedArray()

        val groupModelList = lessonModel.map { model -> model.groupModelList }.flatten().distinct()
        val groupEntityArray = groupModelList.map { model -> model.groupEntity }.toTypedArray()

        val departmentEntityArray = groupModelList
            .map { model -> model.departmentEntity }
            .distinct()
            .toTypedArray()

        val lessonGroupCrossRefEntityArray = lessonModel.map { model ->
            model.groupModelList.map { groupModel ->
                LessonGroupCrossRefEntity(
                    lessonID = model.lessonEntity.id,
                    groupID = groupModel.groupEntity.id
                )
            }
        }.flatten().toTypedArray()


        disciplineDAO.insertOrUpdate(*disciplineEntityArray)
        teacherDAO.insertOrUpdate(*teacherEntityArray)
        buildingDAO.insertOrUpdate(*buildingEntityArray)
        roomDAO.insertOrUpdate(*roomEntityArray)
        departmentDAO.insertOrUpdate(*departmentEntityArray)
        groupDAO.insertOrUpdate(*groupEntityArray)
        lessonDAO.insertOrUpdate(*lessonEntityArray)
        lessonGroupCrossRefDAO.insertOrUpdate(*lessonGroupCrossRefEntityArray)
    }

    suspend fun addLesson(lessonModel: LessonModel) {
        saveLessonModels(lessonModel)
    }

    suspend fun updateLesson(lessonModel: LessonModel) {
        removeLesson(lessonModel.lessonEntity)
        addLesson(lessonModel)
    }

    suspend fun removeLesson(lessonEntity: LessonEntity) {
        lessonDAO.delete(lessonEntity)
    }

    suspend fun startLesson(lessonID: Long, teacherID: Long) {
        val activeLesson = lessonDAO.getTeacherActiveLesson(teacherID)

        if (activeLesson != null) {
            throw LessonException("there is already an active lesson")
        } else {
            val lessonEntity = lessonDAO.getEntity(lessonID) ?: throw LessonException("lesson not found")

            val startedLesson = lessonEntity.copy(
                startTime = DateTimeTz.nowLocal().toLong(),
                state = Lesson.State.ACTIVE,
                isSynchronised = false
            )

            lessonDAO.insertOrUpdate(startedLesson)
        }
    }

    suspend fun finishLesson(lessonID: Long) {
        val lessonEntity = lessonDAO.getEntity(lessonID) ?: throw LessonException("lesson not found")
        val nowTime = DateTimeTz.nowLocal()
        val startTime = lessonEntity.startTime?.toDateTimeTz() ?: throw LessonException("start time is not specified")

        val finishedLesson = lessonEntity.copy(
            actualDuration = (nowTime - startTime).toDouble(),
            state = Lesson.State.FINISHED,
            isSynchronised = false
        )

        lessonDAO.insertOrUpdate(finishedLesson)
    }
}
