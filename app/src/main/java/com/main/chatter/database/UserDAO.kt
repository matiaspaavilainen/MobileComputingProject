package com.main.chatter.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDAO {
    @Query("SELECT * FROM user WHERE userName = :userName")
    fun selectByUsername(userName: String): User?

    @Upsert
    suspend fun upsert(user: User)
}