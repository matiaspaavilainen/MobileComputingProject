package com.main.chatter.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class UserUiState(
    val userName: String = "", val passWord: String = "", val loggedIn: Boolean = false
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appRepository: AppRepository, private val userDataStore: UserDataStore
) : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    fun updateUserName(userName: String) {
        viewModelScope.launch {
            _userUiState.update {
                it.copy(userName = userName)
            }
        }
    }

    fun updatePassWord(passWord: String) {
        viewModelScope.launch {
            _userUiState.update {
                it.copy(passWord = passWord)
            }
        }
    }

    fun addUser() {
        viewModelScope.launch {
            _userUiState.update {
                // Use the values that have been updated by user
                it.copy(
                    userName = userUiState.value.userName,
                    passWord = userUiState.value.passWord,
                    loggedIn = true
                )
            }
            appRepository.addUser(userUiState.value.userName, userUiState.value.passWord)
            // Update the user datastore to keep user logged in
            userDataStore.updateUser(
                userUiState.value.userName, userUiState.value.passWord, true
            )
        }
    }

    /**
     * Returns true if log in successful, false otherwise
     */
    fun logIn(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            // Use the values that have been updated by user
            if (appRepository.logIn(userUiState.value.userName, userUiState.value.passWord)) {
                _userUiState.update {
                    it.copy(
                        userName = userUiState.value.userName,
                        passWord = userUiState.value.passWord,
                        loggedIn = true
                    )
                }
                // Update the user datastore to keep user logged in
                userDataStore.updateUser(
                    userUiState.value.userName, userUiState.value.passWord, true
                )
            }
            callback(userUiState.value.loggedIn)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _userUiState.update {
                it.copy(userName = "", passWord = "", loggedIn = false)
            }
            // remove user from datastore to log out
            userDataStore.updateUser("", "", false)
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return withContext(viewModelScope.launch {
            // ChatGPT: use .collect()
            userDataStore.getUser().collect { (userName, passWord, loggedIn) ->
                if (loggedIn) {
                    _userUiState.update {
                        it.copy(
                            userName = userName, passWord = passWord, loggedIn = true
                        )
                    }
                }
            }
        }) {
            userUiState.value.loggedIn
        }
    }
}