package github.leavesczy.compose_chat.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import github.leavesczy.compose_chat.common.model.*
import github.leavesczy.compose_chat.model.ChatPageState
import github.leavesczy.compose_chat.ui.widgets.CircleImage
import github.leavesczy.compose_chat.ui.widgets.CoilImage

/**
 * @Author: leavesCZY
 * @Date: 2021/10/26 11:00
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun MessagePanel(
    listState: LazyListState,
    chat: Chat,
    contentPadding: PaddingValues,
    chatPageState: ChatPageState,
    onClickAvatar: (Message) -> Unit,
    onClickMessage: (Message) -> Unit,
    onLongClickMessage: (Message) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = contentPadding),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        for (message in chatPageState.messageList) {
            item(key = message.messageDetail.msgId) {
                MessageItems(
                    message = message,
                    showPartyName = chat is GroupChat,
                    onClickAvatar = onClickAvatar,
                    onClickMessage = onClickMessage,
                    onLongClickMessage = onLongClickMessage
                )
            }
        }
        if (chatPageState.showLoadMore) {
            item(key = "loadMore") {
                MessageLoading()
            }
        }
    }
}

@Composable
private fun MessageItems(
    message: Message,
    showPartyName: Boolean,
    onClickAvatar: (Message) -> Unit,
    onClickMessage: (Message) -> Unit,
    onLongClickMessage: (Message) -> Unit,
) {
    if (message is TimeMessage) {
        TimeMessage(message = message)
        return
    }
    val messageContent = @Composable {
        when (message) {
            is TextMessage -> {
                TextMessage(message = message)
            }
            is ImageMessage -> {
                ImageMessage(message = message)
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }
    val isSelfMessage = message.messageDetail.isSelfMessage
    if (isSelfMessage) {
        SelfMessageContainer(
            message = message,
            messageContent = {
                messageContent()
            },
            onClickAvatar = onClickAvatar,
            onClickMessage = onClickMessage,
            onLongClickMessage = onLongClickMessage
        )
    } else {
        FriendMessageContainer(
            message = message,
            showPartyName = showPartyName,
            messageContent = {
                messageContent()
            },
            onClickAvatar = onClickAvatar,
            onClickMessage = onClickMessage,
            onLongClickMessage = onLongClickMessage
        )
    }
}

private val avatarSize = 44.dp
private val itemHorizontalPadding = 10.dp
private val itemVerticalPadding = 10.dp
private val textMessageWidthAtMost = 230.dp
private val textMessageSenderNameVerticalPadding = 3.dp
private val textMessageHorizontalPadding = 8.dp
private val messageShape = RoundedCornerShape(size = 6.dp)
private val timeMessageShape = RoundedCornerShape(size = 4.dp)

@Composable
private fun SelfMessageContainer(
    message: Message,
    messageContent: @Composable (Message) -> Unit,
    onClickAvatar: (Message) -> Unit,
    onClickMessage: (Message) -> Unit,
    onLongClickMessage: (Message) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = itemHorizontalPadding,
                vertical = itemVerticalPadding
            )
    ) {
        val (avatarRefs, showNameRefs, messageRefs, messageStateRefs) = createRefs()
        CircleImage(
            data = message.messageDetail.sender.faceUrl,
            modifier = Modifier
                .constrainAs(ref = avatarRefs) {
                    top.linkTo(anchor = parent.top)
                    end.linkTo(anchor = parent.end)
                }
                .size(size = avatarSize)
                .clickable(onClick = {
                    onClickAvatar(message)
                })
        )
        Text(
            modifier = Modifier
                .constrainAs(ref = showNameRefs) {
                    top.linkTo(
                        anchor = avatarRefs.top
                    )
                    end.linkTo(
                        anchor = avatarRefs.start,
                        margin = textMessageHorizontalPadding
                    )
                },
            text = "",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
        Box(
            modifier = Modifier
                .constrainAs(ref = messageRefs) {
                    top.linkTo(
                        anchor = showNameRefs.bottom,
                        margin = textMessageSenderNameVerticalPadding
                    )
                    end.linkTo(anchor = showNameRefs.end)
                    width = Dimension.preferredWrapContent.atMost(dp = textMessageWidthAtMost)
                }
                .clip(shape = messageShape)
                .combinedClickable(
                    onClick = {
                        onClickMessage(message)
                    },
                    onLongClick = {
                        onLongClickMessage(message)
                    }
                )
        ) {
            messageContent(message)
        }
        StateMessage(
            modifier = Modifier.constrainAs(ref = messageStateRefs) {
                top.linkTo(anchor = messageRefs.top)
                bottom.linkTo(anchor = messageRefs.bottom)
                end.linkTo(anchor = messageRefs.start, margin = textMessageHorizontalPadding)
            },
            messageState = message.messageDetail.state
        )
    }
}

@Composable
private fun FriendMessageContainer(
    message: Message,
    showPartyName: Boolean,
    messageContent: @Composable (Message) -> Unit,
    onClickAvatar: (Message) -> Unit,
    onClickMessage: (Message) -> Unit,
    onLongClickMessage: (Message) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = itemHorizontalPadding,
                vertical = itemVerticalPadding
            )
    ) {
        val (avatarRefs, showNameRefs, messageRefs, messageStateRefs) = createRefs()
        CircleImage(
            data = message.messageDetail.sender.faceUrl,
            modifier = Modifier
                .constrainAs(ref = avatarRefs) {
                    top.linkTo(anchor = parent.top)
                    start.linkTo(anchor = parent.start)
                }
                .size(size = avatarSize)
                .clickable(onClick = {
                    onClickAvatar(message)
                })
        )
        Text(
            modifier = Modifier
                .constrainAs(ref = showNameRefs) {
                    top.linkTo(
                        anchor = avatarRefs.top
                    )
                    start.linkTo(
                        anchor = avatarRefs.end,
                        margin = textMessageHorizontalPadding
                    )
                },
            text = if (showPartyName) {
                message.messageDetail.sender.showName
            } else {
                ""
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start,
        )
        Box(
            modifier = Modifier
                .constrainAs(ref = messageRefs) {
                    top.linkTo(
                        anchor = showNameRefs.bottom,
                        margin = textMessageSenderNameVerticalPadding
                    )
                    start.linkTo(anchor = showNameRefs.start)
                    width = Dimension.preferredWrapContent.atMost(dp = textMessageWidthAtMost)
                }
                .clip(shape = messageShape)
                .combinedClickable(
                    onClick = {
                        onClickMessage(message)
                    },
                    onLongClick = {
                        onLongClickMessage(message)
                    }
                )
        ) {
            messageContent(message)
        }
        StateMessage(
            modifier = Modifier.constrainAs(ref = messageStateRefs) {
                top.linkTo(anchor = messageRefs.top)
                bottom.linkTo(anchor = messageRefs.bottom)
                start.linkTo(anchor = messageRefs.end, margin = textMessageHorizontalPadding)
            },
            messageState = message.messageDetail.state,
        )
    }
}

@Composable
private fun TextMessage(
    message: TextMessage,
) {
    Text(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                horizontal = 6.dp,
                vertical = 6.dp
            ),
        text = message.formatMessage,
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
        textAlign = TextAlign.Start
    )
}

@Composable
private fun TimeMessage(message: TimeMessage) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .background(color = Color.LightGray.copy(alpha = 0.4f), shape = timeMessageShape)
            .padding(all = 3.dp),
        text = message.formatMessage,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
    )
}

@Composable
private fun ImageMessage(message: ImageMessage) {
    val preview = message.preview
    val imageWidth = preview.width
    val imageHeight = preview.height
    val maxImageRatio = 1.9f
    val widgetWidth = 190.dp
    val widgetHeight = if (imageWidth <= 0 || imageHeight <= 0) {
        widgetWidth
    } else {
        widgetWidth * (minOf(maxImageRatio, 1.0f * imageHeight / imageWidth))
    }
    CoilImage(
        modifier = Modifier.size(width = widgetWidth, height = widgetHeight),
        data = preview.url
    )
}

@Composable
private fun StateMessage(modifier: Modifier, messageState: MessageState) {
    when (messageState) {
        MessageState.Sending -> {
            CircularProgressIndicator(
                modifier = modifier.size(size = 20.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }
        is MessageState.SendFailed -> {
            Image(
                modifier = modifier.size(size = 20.dp),
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.Red)
            )
        }
        MessageState.Completed -> {

        }
    }
}

@Composable
private fun MessageLoading() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}