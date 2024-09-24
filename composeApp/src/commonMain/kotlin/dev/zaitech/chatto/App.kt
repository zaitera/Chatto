package dev.zaitech.chatto

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import chatto.composeapp.generated.resources.Res
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.InitializationState
import org.jetbrains.compose.ui.tooling.preview.Preview
import chatto.composeapp.generated.resources.app_name
import io.getstream.chat.android.compose.ui.channels.SearchMode
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    val chatClient = rememberChatClient()
    val clientInitializationState by rememberClientInitializationState(chatClient)
    val navController = rememberNavController()
    ChatTheme {
        NavHost(navController = navController, startDestination = "channels") {
            composable("channels") {
                when (clientInitializationState) {
                    InitializationState.COMPLETE -> {
                        ChannelsScreen(
                            title = stringResource(Res.string.app_name),
                            isShowingHeader = true,
                            searchMode = SearchMode.Channels,
                            onChannelClick = { channel ->
                                navController.navigate("messages/${channel.type}:${channel.id}")
                            },
                            onBackPressed = {
                                navController.popBackStack()
                            },
                        )
                    }

                    InitializationState.INITIALIZING -> {
                        Text(text = "Initializing...")
                    }

                    InitializationState.NOT_INITIALIZED -> {
                        Text(text = "Not initialized...")
                    }
                }
            }
            composable(
                "messages/{channelId}",
                arguments = listOf(navArgument("channelId") { type = NavType.StringType })
            ) { backStackEntry ->
                val channelId = backStackEntry.arguments?.getString("channelId")
                if (channelId != null) {
                    MessagesScreen(
                        viewModelFactory = MessagesViewModelFactory(
                            context = LocalContext.current,
                            channelId = channelId
                        ),
                        onBackPressed = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

