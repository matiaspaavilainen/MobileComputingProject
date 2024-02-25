package com.main.chatter

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.main.chatter.data.UserViewModel

@Composable
fun StartScreen(
    userViewModel: UserViewModel,
    onNavigateToRegistration: () -> Unit,
    onNavigateToChatScreen: () -> Unit
) {

    val userUiState by userViewModel.userUiState.collectAsState()

    var logInFailed by remember {
        mutableStateOf(false)
    }

    var visible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedText("chatter", 80)

        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = logInFailed) {
                Text(
                    text = "Wrong password or username",
                    Modifier.padding(bottom = 3.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            OutlinedTextField(
                value = userUiState.userName,
                onValueChange = { userName ->
                    val trimmedName = userName.trim()
                    userViewModel.updateUserName(trimmedName)
                },
                label = { Text("Username") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            visible = userUiState.userName.length >= 3

            AnimatedVisibility(visible = visible) {
                OutlinedTextField(
                    value = userUiState.passWord,
                    onValueChange = { passWord ->
                        val trimmedPW = passWord.trim()
                        userViewModel.updatePassWord(trimmedPW)
                        logInFailed = false
                    },
                    label = { Text("Password") },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        userViewModel.logIn { success ->
                            if (success) {
                                Log.d("startScreen", userUiState.userName)
                                onNavigateToChatScreen()
                            } else {
                                logInFailed = true
                            }
                        }
                    }),
                    isError = logInFailed
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onNavigateToRegistration() },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(104.dp)
                ) {
                    Text(
                        text = "Register"
                    )
                }

                Button(
                    onClick = {
                        userViewModel.logIn { success ->
                            if (success) {
                                Log.d("startScreen", userUiState.userName)
                                onNavigateToChatScreen()
                            } else {
                                logInFailed = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(104.dp),
                    enabled = userUiState.passWord.isNotEmpty(),
                ) {
                    Text(
                        text = "Login"
                    )
                }
            }
        }
    }
}