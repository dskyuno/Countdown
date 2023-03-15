package com.project.timemanagerment.feature.home.data.repository


import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.source.CountdownPagingSource
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface CountdownRepository {

    suspend fun insertCountdown(countdown: Countdown): Long

    suspend fun deleteCountdown(vararg countdown: Countdown)

    suspend fun updateCountdown(vararg countdown: Countdown)

    fun getCountDownById(countdownId: Long): Flow<Countdown>

    suspend fun getCountDownByIdNotFlow(countdownId: Long): Countdown

    suspend fun getMaxOrderIndexInCountdown(): Long?

    fun getCountdownsByOrderIndexDesc(): Flow<MutableList<Countdown>>
    fun getCountdownsByOrderIndexDescWithPaging(): CountdownPagingSource
    fun getMockgetCountdownsByOrderIndexDesc(): MutableList<Countdown>
    fun getMaxIndexCountdown(): Flow<Countdown?>

    suspend fun getCountdownCount(): Long
    suspend fun findTodayCountdowns(todayDate:Date):MutableList<Countdown>
}