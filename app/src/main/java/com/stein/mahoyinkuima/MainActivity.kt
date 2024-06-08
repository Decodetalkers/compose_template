package com.stein.mahoyinkuima

import android.os.Bundle
import androidx.compose.foundation.layout.padding
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stein.mahoyinkuima.ui.theme.MahoyinkuimaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MahoyinkuimaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        // color = MaterialTheme.colorScheme.background
                        ) { Greeting("Android") }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    MaterialTheme {
        Scaffold(bottomBar = { BottomBar(navController = rememberNavController()) }) { padding ->
            Text(text="I love android${name}", modifier = Modifier.fillMaxSize().padding(padding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MahoyinkuimaTheme { Greeting("Android") }
}

@Composable
private fun BottomBar(
        navController: NavHostController,
) {
    val screens = listOf(BottomBarScreen.Home, BottomBarScreen.Profile, BottomBarScreen.Settings)
    val navBackStackEnty by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEnty?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }

    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
        screen: BottomBarScreen,
        currentDestination: NavDestination?,
        navController: NavHostController
) {
    NavigationBarItem(
            label = { Text(text = screen.title) },
            icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
            // unselectedContentColor = LocalContentColor.current.copy(alpha =
            // ContentAlpha.disabled),
            onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
    )
}
