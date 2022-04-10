package github.leavesczy.compose_chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import github.leavesczy.compose_chat.base.model.ServerState
import github.leavesczy.compose_chat.cache.AccountCache
import github.leavesczy.compose_chat.extend.ProvideNavHostController
import github.leavesczy.compose_chat.extend.navToLogin
import github.leavesczy.compose_chat.logic.AppViewModel
import github.leavesczy.compose_chat.model.AppTheme
import github.leavesczy.compose_chat.model.HomeScreenTab
import github.leavesczy.compose_chat.model.Screen
import github.leavesczy.compose_chat.ui.chat.ChatScreen
import github.leavesczy.compose_chat.ui.chat.GroupProfileScreen
import github.leavesczy.compose_chat.ui.common.PreviewImageScreen
import github.leavesczy.compose_chat.ui.friend.FriendProfileScreen
import github.leavesczy.compose_chat.ui.home.HomeScreen
import github.leavesczy.compose_chat.ui.home.UpdateProfileScreen
import github.leavesczy.compose_chat.ui.login.LoginScreen
import github.leavesczy.compose_chat.ui.theme.ChatTheme
import github.leavesczy.compose_chat.ui.widgets.SetSystemBarsColor
import github.leavesczy.compose_chat.utils.showToast

/**
 * @Author: leavesCZY
 * @Date: 2021/6/23 21:59
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val appViewModel = viewModel<AppViewModel>()
            val appTheme by appViewModel.appTheme.collectAsState()
            val switchToNextTheme = remember {
                {
                    appViewModel.switchToNextTheme()
                }
            }
            val navController = rememberAnimatedNavController()
            LaunchedEffect(key1 = Unit) {
                appViewModel.serverConnectState.collect {
                    when (it) {
                        ServerState.KickedOffline -> {
                            showToast("本账号已在其它客户端登陆，请重新登陆")
                            AccountCache.onUserLogout()
                            navController.navToLogin()
                        }
                        ServerState.Logout -> {
                            navController.navToLogin()
                        }
                        else -> {
                            showToast("Connect State Changed : $it")
                        }
                    }
                }
            }
            ChatTheme(appTheme = appTheme) {
                SetSystemBarsColor(
                    statusBarColor = Color.Transparent,
                    statusBarDarkIcons = appTheme == AppTheme.Light || appTheme == AppTheme.Gray,
                    navigationBarColor = MaterialTheme.colorScheme.background,
                    navigationDarkIcons = appTheme != AppTheme.Dark
                )
                NavigationView(
                    navController = navController,
                    appTheme = appTheme,
                    switchToNextTheme = switchToNextTheme
                )
                if (appTheme == AppTheme.Gray) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            color = Color.LightGray,
                            blendMode = BlendMode.Saturation
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun NavigationView(
        navController: NavHostController,
        appTheme: AppTheme,
        switchToNextTheme: () -> Unit
    ) {
        var homeScreenSelected by remember {
            mutableStateOf(HomeScreenTab.Conversation)
        }
        ProvideNavHostController(navHostController = navController) {
            AnimatedNavHost(
                modifier = Modifier.navigationBarsPadding(),
                navController = navController,
                startDestination = Screen.LoginScreen.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = {
                            -it
                        },
                        animationSpec = tween(200)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = {
                            it
                        },
                        animationSpec = tween(200)
                    )
                },
            ) {
                animatedComposable(
                    screen = Screen.LoginScreen,
                ) {
                    LoginScreen()
                }
                animatedComposable(screen = Screen.HomeScreen) {
                    HomeScreen(
                        appTheme = appTheme,
                        switchToNextTheme = switchToNextTheme,
                        homeTabSelected = homeScreenSelected,
                        onHomeTabSelected = {
                            homeScreenSelected = it
                        }
                    )
                }
                animatedComposable(screen = Screen.FriendProfileScreen) { backStackEntry ->
                    FriendProfileScreen(
                        friendId = Screen.FriendProfileScreen.getArgument(backStackEntry)
                    )
                }
                animatedComposable(screen = Screen.ChatScreen) { backStackEntry ->
                    val listState = rememberLazyListState()
                    ChatScreen(
                        listState = listState,
                        chat = Screen.ChatScreen.getArgument(backStackEntry),
                    )
                }
                animatedComposable(screen = Screen.GroupProfileScreen) { backStackEntry ->
                    GroupProfileScreen(
                        groupId = Screen.GroupProfileScreen.getArgument(backStackEntry),
                    )
                }
                animatedComposable(screen = Screen.PreviewImageScreen) { backStackEntry ->
                    PreviewImageScreen(
                        imagePath = Screen.PreviewImageScreen.getArgument(
                            backStackEntry
                        ),
                    )
                }
                animatedComposable(screen = Screen.UpdateProfileScreen) {
                    UpdateProfileScreen()
                }
            }
        }
    }

    private fun NavGraphBuilder.animatedComposable(
        screen: Screen,
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
    ) {
        composable(
            route = screen.route
        ) { backStackEntry ->
            content(backStackEntry)
        }
    }

}