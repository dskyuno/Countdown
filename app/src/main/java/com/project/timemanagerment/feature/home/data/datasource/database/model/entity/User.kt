package com.project.timemanagerment.feature.home.data.datasource.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val userName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L
}