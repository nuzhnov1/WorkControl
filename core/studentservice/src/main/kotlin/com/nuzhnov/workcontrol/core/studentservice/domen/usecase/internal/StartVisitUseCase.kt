package com.nuzhnov.workcontrol.core.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.studentservice.domen.repository.StudentServiceRepository
import java.net.InetAddress
import javax.inject.Inject

internal class StartVisitUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    suspend operator fun invoke(serverAddress: InetAddress, serverPort: Int) {
        repository.startVisit(serverAddress, serverPort)
    }
}
