package com.nuzhnov.workcontrol.core.works.data.work.complex

import com.nuzhnov.workcontrol.core.works.data.work.clear.*
import javax.inject.Inject

internal class ClearLocalDataWork @Inject constructor(
    private val clearBuildingsWork: ClearBuildingsWork,
    private val clearRoomsWork: ClearRoomsWork,
    private val clearDepartmentsWork: ClearDepartmentsWork,
    private val clearGroupsWork: ClearGroupsWork,
    private val clearStudentsWork: ClearStudentsWork,
    private val clearTeachersWork: ClearTeachersWork,
    private val clearDisciplinesWork: ClearDisciplinesWork,
    private val clearLessonsWork: ClearLessonsWork
) {

    suspend operator fun invoke(): Result<Unit> {
        val workResultList = listOf(
            clearLessonsWork(),
            clearStudentsWork(),
            clearGroupsWork(),
            clearDepartmentsWork(),
            clearRoomsWork(),
            clearBuildingsWork(),
            clearTeachersWork(),
            clearDisciplinesWork()
        )

        return workResultList.firstOrNull { result -> result.isFailure } ?: Result.success(Unit)
    }
}
