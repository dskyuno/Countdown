package com.project.timemanagerment.feature.home.data.source

import androidx.lifecycle.asLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.timemanagerment.feature.home.data.datasource.database.dao.CountdownDao
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CountdownDatasource @Inject constructor(private val countdownDao: CountdownDao) {

    suspend fun insertCountdown(countdown: Countdown) =
        withContext(Dispatchers.IO) {
            return@withContext countdownDao.insertCountdown(countdown)
        }


    suspend fun deleteCountdown(vararg countdown: Countdown) {
        withContext(Dispatchers.IO) {
            countdownDao.deleteCountdown(*countdown)
        }
    }

    suspend fun updateCountdown(vararg countdown: Countdown) {
        withContext(Dispatchers.IO) {
            countdownDao.updateCountdown(*countdown)
        }

    }

    fun getCountDownById(countdownId: Long): Flow<Countdown> {
        return countdownDao.getCountDownById(countdownId)

    }

    suspend fun getCountDownByIdNotFlow(countdownId: Long) = withContext(Dispatchers.IO) {
        countdownDao.getCountDownByIdNotFlow(countdownId)
    }

    suspend fun getMaxOrderIndexInCountdown() = withContext(Dispatchers.IO) {
        countdownDao.getMaxOrderIndexInCountdown()
    }

    fun getCountdownsByOrderIndexDesc(): Flow<MutableList<Countdown>> {
        return countdownDao.getCountdownsByOrderIndexDesc()
    }

    fun getCountdownsByOrderIndexDescWithPaging(): CountdownPagingSource {
        return CountdownPagingSource(countdownDao)
    }

    fun getMaxIndexCountdown(): Flow<Countdown?> {
        return countdownDao.getMaxIndexCountdown()
    }

    fun getMockgetCountdownsByOrderIndexDesc(): MutableList<Countdown> {
        val list = mutableListOf<Countdown>()
        return list
    }

    suspend fun getCountdownCount() = withContext(Dispatchers.IO) {
        countdownDao.getCountdownCount()
    }

   suspend fun findTodayCountdowns(todayDate: Date): MutableList<Countdown> {
        return  countdownDao.findTodayCountdowns(todayDate)
    }


}

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3_000L

class CountdownPagingSource constructor(private val countdownDao: CountdownDao) :
    PagingSource<Int, Countdown>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Countdown> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            //val response = backend.searchUsers(query, nextPageNumber)
            val list = countdownDao.getCountdownsByOrderIndexDescNotFlow()
            return LoadResult.Page(
                data = list,
                prevKey = null, // Only paging forward.
                nextKey = null
            )
        } catch (e: Exception) {
            return LoadResult.Error(
                Throwable("error")
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Countdown>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}