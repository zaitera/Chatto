package dev.zaitech.chatto

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.InitializationState
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory

@Composable
fun rememberClientInitializationState(client: ChatClient): State<InitializationState> {
    return client.clientState.initializationState.collectAsState()
}

@Composable
fun rememberChatClient(): ChatClient {
    val context = LocalContext.current

    // Initialize plugins
    val offlinePluginFactory = remember {
        StreamOfflinePluginFactory(appContext = context)
    }
    val statePluginFactory = remember {
        StreamStatePluginFactory(StatePluginConfig(), appContext = context)
    }

    // Build the ChatClient
    val client = remember {
        ChatClient.Builder(Secrets.STREAM_API_KEY, context)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()
    }

    // Connect the user
    LaunchedEffect(client) {
        val user = User(
            id = "leandro",
            name = "Leandro Borges Ferreira",
            image = "https://ca.slack-edge.com/T02RM6X6B-U01AQ67NJ9Z-2f28d711cae9-128",
        )
        client.connectUser(
            user = user,
            token = Secrets.STREAM_CLIENT_TOKEN,
        ).enqueue{ result ->
            if (result.isSuccess) {
                Log.d("ChatClient", "User connected successfully")
            } else {
                Log.e("ChatClient", "Error connecting user: $result")
            }
        }
    }

    return client
}