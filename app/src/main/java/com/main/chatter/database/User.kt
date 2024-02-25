package com.main.chatter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class User(
    @PrimaryKey val userName: String,
    val passWord: String,
)
