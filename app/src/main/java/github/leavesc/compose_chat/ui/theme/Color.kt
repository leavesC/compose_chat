package github.leavesc.compose_chat.ui.theme

import androidx.compose.ui.graphics.Color
import github.leavesc.compose_chat.cache.AppThemeCache
import github.leavesc.compose_chat.model.AppTheme

val BackgroundColorLight = Color(0xFFFFFFFF)
val PrimaryColorLight = Color(0xFFEF5350)//按钮背景色
val PrimaryVariantColorLight = Color(0xFFFFFFFF)//标题栏、导航栏背景色
val SurfaceColorLight = Color(0xFFEF5350)//图标着色
val SecondaryColorLight = Color(0xFFF4F4F4)

val BackgroundColorDark = Color(0xFF222222)
val PrimaryColorDark = Color(0xFFE05E55)
val PrimaryVariantColorDark = Color(0xFF222222)
val SurfaceColorDark = Color(0xFFE05E55)
val SecondaryColorDark = Color(0xFF0E0E0F)

val BackgroundColorPink = Color(0xFFFFFFFF)
val PrimaryColorPink = Color(0xFFEC407A)
val PrimaryVariantColorPink = Color(0xFFEC407A)
val SurfaceColorPink = Color(0xFFFFFFFF)
val SecondaryColorPink = Color(0xFFF4F4F4)

val friendMsgBgColorLight = Color(0x80009688)
val friendMsgBgColorDark = Color(0x4D009688)
val friendMsgBgColorPink = Color(0xDA1E88E5)

val selfMsgBgColorLight = Color(0x80FF0000)
val selfMsgBgColorDark = Color(0x4DFF0000)
val selfMsgBgColorPink = PrimaryColorPink.copy(alpha = 0.8f)

val friendMsgBgColor: Color
    get() {
        return when (AppThemeCache.currentTheme) {
            AppTheme.Light -> friendMsgBgColorLight
            AppTheme.Dark -> friendMsgBgColorDark
            AppTheme.Pink -> friendMsgBgColorPink
        }
    }

val selfMsgBgColor: Color
    get() {
        return when (AppThemeCache.currentTheme) {
            AppTheme.Light -> selfMsgBgColorLight
            AppTheme.Dark -> selfMsgBgColorDark
            AppTheme.Pink -> selfMsgBgColorPink
        }
    }