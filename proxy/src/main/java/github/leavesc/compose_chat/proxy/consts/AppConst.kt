package github.leavesc.compose_chat.proxy.consts

import github.leavesc.compose_chat.base.model.PersonProfile
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @Author: leavesC
 * @Date: 2021/6/7 19:38
 * @Desc:
 * @Github：https://github.com/leavesC
 */
internal object AppConst {

    const val APP_ID = 1400592743

    const val APP_SECRET_KEY = "9b9d7ea10c1d88a377e31b19320ed8780a12f55451a76461b3a87189ee7339e0"

    val personProfile = MutableStateFlow(PersonProfile.Empty)

}