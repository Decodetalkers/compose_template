package com.stein.mahoyinkuima

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomBarScreen(route = "HOME", title = "HOME", icon = Icons.Default.Home)

    data object Settings :
            BottomBarScreen(route = "SETTINGS", title = "SETTINGS", icon = Icons.Default.Settings)
}

sealed class OverViewScreen(val route: String) {
    data object Main : OverViewScreen("MAIN")
    data object News : OverViewScreen("News")
}
