package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.studentservice.domen.repository.StudentServiceRepository
import java.net.InetAddress
import javax.inject.Inject

internal class StartVisitUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    suspend operator fun invoke(serverAddress: InetAddress, serverPort: Int) {
        repository.startVisit(serverAddress, serverPort)
    }
}
