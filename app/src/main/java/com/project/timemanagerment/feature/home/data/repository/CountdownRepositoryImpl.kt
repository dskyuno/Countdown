package com.project.timemanagerment.feature.home.data.repository

import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.source.CountdownDatasource
import com.project.timemanagerment.feature.home.data.source.CountdownPagingSource
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class CountdownRepositoryImpl @Inject constructor(private val countdownDataSource: CountdownDatasource) :
    CountdownRepository {
    override suspend fun insertCountdown(countdown: Countdown): Long {
        val nowMaxIndex =
            if (getMaxOrderIndexInCountdown() != null) getMaxOrderIndexInCountdown()!! + 1 else 1
        countdown.orderIndex = nowMaxIndex
        return countdownDataSource.insertCountdown(countdown)
    }

    override suspend fun deleteCountdown(vararg countdown: Countdown) {
        countdownDataSource.deleteCountdown(*countdown)
    }

    override suspend fun updateCountdown(vararg countdown: Countdown) {
        countdownDataSource.updateCountdown(*countdown)
    }

    override fun getCountDownById(countdownId: Long): Flow<Countdown> {
        return countdownDataSource.getCountDownById(countdownId)

    }

    override suspend fun getCountDownByIdNotFlow(countdownId: Long): Countdown {
        return countdownDataSource.getCountDownByIdNotFlow(countdownId)
    }

    override suspend fun getMaxOrderIndexInCountdown(): Long? {
        return countdownDataSource.getMaxOrderIndexInCountdown()
    }

    override fun getCountdownsByOrderIndexDescWithPaging(): CountdownPagingSource {
        return countdownDataSource.getCountdownsByOrderIndexDescWithPaging()
    }

    override fun getCountdownsByOrderIndexDesc(): Flow<MutableList<Countdown>> {
        return countdownDataSource.getCountdownsByOrderIndexDesc()
    }

    override fun getMaxIndexCountdown(): Flow<Countdown?> {
        return countdownDataSource.getMaxIndexCountdown()
    }

    override fun getMockgetCountdownsByOrderIndexDesc(): MutableList<Countdown> {
        val list = mutableListOf<Countdown>()
        return list
    }

    override suspend fun getCountdownCount(): Long {
        return countdownDataSource.getCountdownCount()
    }

    override suspend fun findTodayCountdowns(todayDate: Date): MutableList<Countdown> {
        return countdownDataSource.findTodayCountdowns(todayDate)
    }
}