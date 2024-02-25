package com.main.chatter.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.main.chatter.NotificationHandler
import com.main.chatter.database.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagesUiState(
    val messages: Flow<List<Message>> = flowOf()
)

data class SingleMessageUiState(
    val author: String = "", val content: String = "", val timestamp: Long = 0
)

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val appRepository: AppRepository, private val notificationHandler: NotificationHandler
) : ViewModel() {

    private val _messagesUiState = MutableStateFlow(MessagesUiState())
    val messagesUiState: StateFlow<MessagesUiState> = _messagesUiState.asStateFlow()

    private val _singleMessageUiState = MutableStateFlow(SingleMessageUiState())
    val singleMessageUiState: StateFlow<SingleMessageUiState> = _singleMessageUiState.asStateFlow()

    private var fetchJob: Job? = null

    fun updateContent(content: String) {
        viewModelScope.launch {
            _singleMessageUiState.update {
                it.copy(content = content)
            }
        }
    }

    fun updateAuthor(author: String) {
        viewModelScope.launch {
            _singleMessageUiState.update {
                it.copy(author = author)
            }
        }
    }

    fun getMessages() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _messagesUiState.update {
                it.copy(messages = appRepository.observeAll())
            }
        }
    }

    fun addMessage() {
        viewModelScope.launch {
            appRepository.createMessage(
                singleMessageUiState.value.author,
                singleMessageUiState.value.content,
                System.currentTimeMillis(),
            )
            val newMessage = appRepository.getLastMessage()

            notificationHandler.showNotification(newMessage)
            updateContent("")
        }
    }
}