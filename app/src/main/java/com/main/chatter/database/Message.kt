package com.main.chatter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"
)
data class Message(
    val author: String,
    val content: String,
    val timeStamp: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
