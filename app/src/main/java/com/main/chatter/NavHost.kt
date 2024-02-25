package com.main.chatter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.main.chatter.data.MessageViewModel
import com.main.chatter.data.UserViewModel

@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val messageViewModel: MessageViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        userViewModel.isLoggedIn()
    }

    NavHost(
        navController = navController, startDestination = "StartScreen"
    ) {
        composable("StartScreen") {
            StartScreen(userViewModel, onNavigateToRegistration = {
                navController.navigate("RegistrationScreen")
            }, onNavigateToChatScreen = {
                navController.navigate("ChatScreen") {
                    popUpTo("StartScreen") {
                        inclusive = true
                    }
                }
            })
        }

        composable("RegistrationScreen") {
            RegistrationScreen(userViewModel, onNavigateToChatScreen = {
                navController.navigate("ChatScreen") {
                    popUpTo("StartScreen") {
                        inclusive = true
                    }
                }
            })
        }

        composable("ChatScreen") {
            ChatScreen(userViewModel, messageViewModel, onNavigateToStartScreen = {
                navController.navigate("StartScreen") {
                    popUpTo("StartScreen") {
                        inclusive = true
                    }
                }
            })
        }
    }
}