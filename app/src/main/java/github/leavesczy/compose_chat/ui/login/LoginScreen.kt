package github.leavesczy.compose_chat.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.leavesczy.compose_chat.R
import github.leavesczy.compose_chat.extend.LocalNavHostController
import github.leavesczy.compose_chat.extend.navigateWithBack
import github.leavesczy.compose_chat.logic.LoginViewModel
import github.leavesczy.compose_chat.model.Screen
import github.leavesczy.compose_chat.ui.widgets.CommonButton
import github.leavesczy.compose_chat.ui.widgets.CommonOutlinedTextField
import github.leavesczy.compose_chat.utils.showToast
import kotlinx.coroutines.launch

/**
 * @Author: leavesCZY
 * @Date: 2021/7/4 1:07
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
@Composable
fun LoginScreen() {
    val loginViewModel = viewModel<LoginViewModel>()
    val loginScreenState by loginViewModel.loginScreenState.collectAsState()
    val textInputService = LocalTextInputService.current
    val navHostController = LocalNavHostController.current
    LaunchedEffect(key1 = Unit) {
        launch {
            loginViewModel.loginScreenState.collect {
                if (it.loginSuccess) {
                    navHostController.navigateWithBack(
                        screen = Screen.HomeScreen
                    )
                    return@collect
                }
            }
        }
        loginViewModel.autoLogin()
    }
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (loginScreenState.showLogo) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.20f)
                            .wrapContentSize(align = Alignment.BottomCenter),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 38.sp,
                        fontFamily = FontFamily.Cursive,
                        textAlign = TextAlign.Center,
                    )
                }
                if (loginScreenState.showInput) {
                    var userId by remember { mutableStateOf(loginScreenState.lastLoginUserId) }
                    CommonOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp, start = 40.dp, end = 40.dp),
                        value = userId,
                        onValueChange = { value ->
                            val realValue = value.trimStart().trimEnd()
                            if (realValue.length <= 12 && realValue.all { it.isLowerCase() || it.isUpperCase() }) {
                                userId = realValue
                            }
                        },
                        label = "UserId",
                        maxLines = 1,
                        singleLine = true
                    )
                    CommonButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        text = "Go"
                    ) {
                        if (userId.isBlank()) {
                            showToast("请输入 UserID")
                        } else {
                            textInputService?.hideSoftwareKeyboard()
                            loginViewModel.goToLogin(userId = userId)
                        }
                    }
                }
            }
            if (loginScreenState.showLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(size = 60.dp)
                        .wrapContentSize(align = Alignment.Center),
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}