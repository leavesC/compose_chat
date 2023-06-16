package github.leavesczy.compose_chat.ui.conversation.logic

import androidx.compose.foundation.lazy.LazyListState
import github.leavesczy.compose_chat.base.model.Conversation

data class ConversationPageViewState(
    val listState: LazyListState,
    val conversationList: List<Conversation>,
    val onClickConversation: (Conversation) -> Unit,
    val deleteConversation: (Conversation) -> Unit,
    val pinConversation: (Conversation, Boolean) -> Unit
)