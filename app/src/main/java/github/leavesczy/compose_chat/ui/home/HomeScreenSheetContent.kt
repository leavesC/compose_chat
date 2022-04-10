package github.leavesczy.compose_chat.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.leavesczy.compose_chat.logic.ComposeChat
import github.leavesczy.compose_chat.model.HomeScreenSheetContentState
import github.leavesczy.compose_chat.ui.widgets.CommonButton
import github.leavesczy.compose_chat.ui.widgets.CommonOutlinedTextField
import github.leavesczy.compose_chat.utils.showToast
import kotlinx.coroutines.launch

/**
 * @Author: leavesCZY
 * @Date: 2021/7/10 15:51
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun HomeScreenSheetContent(
    homeScreenSheetContentState: HomeScreenSheetContentState,
) {
    val coroutineScope = rememberCoroutineScope()

    fun expandSheetContent(targetValue: ModalBottomSheetValue) {
        coroutineScope.launch {
            homeScreenSheetContentState.modalBottomSheetState.animateTo(targetValue = targetValue)
        }
    }

    BackHandler(enabled = homeScreenSheetContentState.modalBottomSheetState.isVisible, onBack = {
        when (homeScreenSheetContentState.modalBottomSheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> {

            }
            ModalBottomSheetValue.Expanded -> {
                expandSheetContent(targetValue = ModalBottomSheetValue.HalfExpanded)
            }
            ModalBottomSheetValue.HalfExpanded -> {
                expandSheetContent(targetValue = ModalBottomSheetValue.Hidden)
            }
        }
    })

    var userId by remember(key1 = homeScreenSheetContentState.modalBottomSheetState.isVisible) {
        mutableStateOf(
            ""
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.8f)
    ) {
        LazyColumn {
            item {
                CommonOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp
                        ),
                    value = userId,
                    onValueChange = { id ->
                        val realValue = id.trimStart().trimEnd()
                        if (realValue.all { it.isLowerCase() || it.isUpperCase() }) {
                            userId = realValue
                        }
                    },
                    label = "输入 UserID",
                    singleLine = true,
                    maxLines = 1,
                )
                CommonButton(text = "添加好友") {
                    if (userId.isBlank()) {
                        showToast("请输入 UserID")
                    } else {
                        homeScreenSheetContentState.toAddFriend(userId)
                    }
                }
                CommonButton(text = "加入交流群 0x01") {
                    homeScreenSheetContentState.toJoinGroup(ComposeChat.groupId01)
                }
                CommonButton(text = "加入交流群 0x02") {
                    homeScreenSheetContentState.toJoinGroup(ComposeChat.groupId02)
                }
                CommonButton(text = "加入交流群 0x03") {
                    homeScreenSheetContentState.toJoinGroup(ComposeChat.groupId03)
                }
            }
        }
    }
}