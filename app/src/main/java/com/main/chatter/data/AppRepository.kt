package com.main.chatter.data

import com.main.chatter.database.Message
import com.main.chatter.database.MessageDAO
import com.main.chatter.database.User
import com.main.chatter.database.UserDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import javax.inject.Inject


/**
 * Based on How to build a data layer by Android Developers https://www.youtube.com/watch?v=P125nWICYps
 * Used to interact with the database
 */
class AppRepository @Inject constructor(
    private val messageDAO: MessageDAO,
    private val userDAO: UserDAO,
) {

    fun observeAll(): Flow<List<Message>> {
        return messageDAO.observerAll()
    }

    suspend fun getLastMessage(): Message {
        return withContext(Dispatchers.IO) {
            messageDAO.getLastMessage()
        }
    }

    suspend fun createMessage(author: String, content: String, timeStamp: Long) {
        return withContext(Dispatchers.IO) {
            val message = Message(
                author = author, content = content, timeStamp = timeStamp
            )
            messageDAO.insert(message)
        }
    }

    /**
     * Returns true if user added, false if username exists or was empty or if password was empty.
     */
    suspend fun addUser(userName: String, passWord: String): Boolean {
        return withContext(Dispatchers.IO) {
            if (userName.isEmpty() || userDAO.selectByUsername(userName)?.userName == userName || passWord.isEmpty()) {
                return@withContext false
            }
            val hashedPassword = hashPassword(passWord)

            val user = User(
                userName = userName,
                passWord = hashedPassword,
            )

            userDAO.upsert(user)
            return@withContext true
        }
    }

    /**
     * Returns SHA-256 encrypted string.
     */
    private fun hashPassword(passWord: String): String {
        val bytes = passWord.toByteArray()
        val sha256 = MessageDigest.getInstance("SHA-256")
        // ChatGPT
        return sha256.digest(bytes).joinToString("") { "%02x".format(it) }
    }

    /**
     * Returns true if password is correct, false otherwise or if user does not exist.
     */
    suspend fun logIn(userName: String, passWord: String): Boolean {
        return withContext(Dispatchers.IO) {
            val hashedPassword = hashPassword(passWord)
            // check id user is null
            val user = userDAO.selectByUsername(userName) ?: return@withContext false
            // check if password is correct
            return@withContext user.passWord == hashedPassword
        }
    }
}