package com.main.chatter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.main.chatter.data.MessageViewModel
import com.main.chatter.data.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userViewModel: UserViewModel,
    messageViewModel: MessageViewModel,
    onNavigateToStartScreen: () -> Unit
) {
    val userUiState by userViewModel.userUiState.collectAsState()

    val messagesUiState by messageViewModel.messagesUiState.collectAsState()

    val singleMessageUiState by messageViewModel.singleMessageUiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    // Fetch messages form db
    LaunchedEffect(Unit) {
        messageViewModel.getMessages()
    }

    val messages by messagesUiState.messages.collectAsState(initial = emptyList())

    // Set the author as the currently logged in user when the screen is opened
    LaunchedEffect(Unit) {
        messageViewModel.updateAuthor(userUiState.userName)
    }

    // Check if the message is not empty and set the messageOk to true to allow the user to send the message
    var messageOk by remember {
        mutableStateOf(false)
    }

    if (singleMessageUiState.content.isNotEmpty()) {
        messageOk = true
    }

    val lazyListState = rememberLazyListState()

    // Automatically scroll to bottom when new message appears in the list of messages
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(17.dp),
            )
            Divider()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 0.dp), Arrangement.Bottom, Alignment.End
            ) {
                NavigationDrawerItem(label = {
                    Text(
                        text = "Log out", color = MaterialTheme.colorScheme.error
                    )
                }, selected = true, onClick = {
                    userViewModel.logOut()
                    onNavigateToStartScreen()
                }, modifier = Modifier.padding(16.dp), icon = {
                    Icon(
                        Icons.Rounded.ExitToApp,
                        contentDescription = "Log out",
                        tint = MaterialTheme.colorScheme.error
                    )
                })
            }
        }
    }, content = {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            Arrangement.Top,
            Alignment.CenterHorizontally
        ) {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = userUiState.userName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }, navigationIcon = {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings, contentDescription = "Settings"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
            )

            // Lazy column for rendering messages easily
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 4.dp),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                items(messages) { message ->
                    MessageCard(msg = message)
                }
            }

            // Row as a bottom bar with text input and send button
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                Arrangement.Absolute.SpaceEvenly,
                Alignment.Top
            ) {
                TextField(
                    value = singleMessageUiState.content,
                    onValueChange = { content ->
                        messageViewModel.updateContent(content)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                        .padding(top = 8.dp, bottom = 8.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = { messageViewModel.addMessage() },
                            // Stop the user from sending empty messages
                            enabled = singleMessageUiState.content.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Send,
                                contentDescription = "Send the text button"
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "Message"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        // Stop the user from sending empty messages
                        imeAction = if (singleMessageUiState.content.isNotEmpty()) ImeAction.Send else ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onSend = { messageViewModel.addMessage() }),
                    maxLines = 8,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    })


}