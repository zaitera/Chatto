package dev.zaitech.chatto

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import kotlin.reflect.KProperty

object StreamChat {
    fun getChatClient(): ChatClient {
        // Set up the OfflinePlugin for offline storage
        val appContext = getAppContext()
        val offlinePluginFactory = StreamOfflinePluginFactory(appContext = appContext)
        val statePluginFactory = StreamStatePluginFactory(config = StatePluginConfig(), appContext = appContext)
        // Set up the client for API calls and with the plugin for offline storage
        val client = ChatClient.Builder("uun7ywwamhs9", appContext)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()
        return client
    }
}

operator fun ChatClient.getValue(thisObj: Any?, property: KProperty<*>): ChatClient {
    return this
}