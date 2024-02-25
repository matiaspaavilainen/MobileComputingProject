package com.main.chatter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
fun RegistrationScreen(
    userViewModel: UserViewModel,
    onNavigateToChatScreen: () -> Unit
) {
    val userUiState by userViewModel.userUiState.collectAsState()

    var repeatPassword by remember {
        mutableStateOf("")
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

        AnimatedText("register", 80)

        Column(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(visible = repeatPassword.isNotEmpty() && repeatPassword != userUiState.passWord) {
                Text(
                    text = "Passwords don't match!",
                    Modifier
                        .padding(bottom = 3.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            OutlinedTextField(
                value = userUiState.userName,
                onValueChange = { userName ->
                    val trimmedName = userName.trim()
                    if (trimmedName.length <= 32) userViewModel.updateUserName(trimmedName)
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
                        if (trimmedPW.length <= 32) userViewModel.updatePassWord(trimmedPW)
                    },
                    label = { Text("Password") },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )
                )
            }

            AnimatedVisibility(visible = userUiState.passWord.length >= 8) {
                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { newPassword ->
                        val trimmedNewPW = newPassword.trim()
                        if (trimmedNewPW.length <= 32) {
                            repeatPassword = trimmedNewPW
                        }
                    },
                    label = { Text("Re-enter Password") },
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            userViewModel.addUser()
                            onNavigateToChatScreen()
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    userViewModel.addUser()
                    onNavigateToChatScreen()
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .width(104.dp),
                enabled = userUiState.passWord.isNotEmpty() && repeatPassword == userUiState.passWord,
            ) {
                Text(
                    text = "Register"
                )
            }
        }
    }
}