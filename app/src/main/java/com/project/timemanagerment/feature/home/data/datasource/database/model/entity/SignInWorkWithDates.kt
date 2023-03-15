package com.project.timemanagerment.feature.home.data.datasource.database.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SignInWorkWithDates(
    @Embedded
    val signInWork: SignInWork,
    @Relation(
        parentColumn = "signInWorkId",
        entityColumn = "parentWorkId"
    )
    val signDates: List<SignInDate>
)
