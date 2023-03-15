package com.project.timemanagerment.feature.home.data.datasource.database.dao

import androidx.room.*
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface CountdownDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountdown(countdown: Countdown): Long

    @Delete
    suspend fun deleteCountdown(vararg countdown: Countdown)

    @Update
    suspend fun updateCountdown(vararg countdown: Countdown)

    @Query("select * from countdown where countdownId =:countdownId")
    fun getCountDownById(countdownId: Long): Flow<Countdown>

    @Query("select * from countdown where countdownId =:countdownId")
    suspend fun getCountDownByIdNotFlow(countdownId: Long): Countdown

    @Query("select max(orderIndex) as orderIndex from countdown")
    suspend fun getMaxOrderIndexInCountdown(): Long?

    @Query("select * from countdown order by orderIndex desc")
    fun getCountdownsByOrderIndexDesc(): Flow<MutableList<Countdown>>

    @Query("select * from countdown order by orderIndex desc")
    suspend fun getCountdownsByOrderIndexDescNotFlow(): MutableList<Countdown>

    @Query("select *  from countdown ORDER BY orderIndex DESC limit 1")
    fun getMaxIndexCountdown(): Flow<Countdown?>

    @Query("select count(countdownId) from countdown")
    suspend fun getCountdownCount(): Long

    @Query("select  * from countdown where targetTime = :todayDate")
    suspend fun findTodayCountdowns(todayDate: Date): MutableList<Countdown>
}