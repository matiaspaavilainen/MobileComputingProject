package com.main.chatter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDAO {
    @Query("SELECT * FROM message")
    fun observerAll(): Flow<List<Message>>

    @Query("SELECT * FROM message ORDER BY id DESC LIMIT 1")
    fun getLastMessage(): Message

    @Insert
    suspend fun insert(message: Message)
}