package github.leavesczy.compose_chat.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import github.leavesczy.compose_chat.cache.AppThemeCache
import github.leavesczy.compose_chat.model.AppTheme

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColorLight,
    onPrimary = OnPrimaryColorLight,
    primaryContainer = PrimaryContainerColorLight,
    surface = SurfaceColorLight,
    onSurface = OnSurfaceColorLight,
    background = BackgroundColorLight,
    onSecondaryContainer = onSecondaryContainerLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = OnPrimaryColorDark,
    primaryContainer = PrimaryContainerColorDark,
    surface = SurfaceColorDark,
    onSurface = OnSurfaceColorDark,
    background = BackgroundColorDark,
    onSecondaryContainer = onSecondaryContainerDark
)

@Composable
fun ChatTheme(
    appTheme: AppTheme = AppThemeCache.currentTheme,
    content: @Composable () -> Unit
) {
    val colors: ColorScheme
    val typography: Typography
    when (appTheme) {
        AppTheme.Light -> {
            colors = LightColorScheme
            typography = LightTypography
        }
        AppTheme.Dark -> {
            colors = DarkColorScheme
            typography = LightTypography
        }
        AppTheme.Gray -> {
            colors = LightColorScheme
            typography = LightTypography
        }
    }
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}