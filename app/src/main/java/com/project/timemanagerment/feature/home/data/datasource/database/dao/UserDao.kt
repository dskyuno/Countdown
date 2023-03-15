package com.project.timemanagerment.feature.home.data.datasource.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("select * from user")
    fun selectALlUser(): Flow<MutableList<User>>
}