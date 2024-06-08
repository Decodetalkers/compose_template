package com.stein.mahoyinkuima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    val navController = rememberNavController()
    MaterialTheme {
        Scaffold(bottomBar = { BottomBar(navController = rememberNavController()) }) { padding ->
            NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
                composable(BottomBarScreen.Home.route) {
                    Text(
                            text = "I love android${name}",
                            modifier = Modifier.fillMaxSize().padding(padding)
                    )
                }
            }
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
    var selectedDestion by remember { mutableIntStateOf(0) }

    val screens = listOf(BottomBarScreen.Home, BottomBarScreen.Profile, BottomBarScreen.Settings)

    val callback =
            NavController.OnDestinationChangedListener end@{ _, destination, _ ->
                if (destination.route == null) return@end
                val index = screens.withIndex().first { destination.route == it.value.route }.index
                if (index >= 0) selectedDestion = index
            }
    navController.addOnDestinationChangedListener(callback)
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            AddItem(
                    screen = screen,
                    isSelected = index == selectedDestion,
                    navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
        screen: BottomBarScreen,
        isSelected: Boolean,
        navController: NavHostController
) {
    NavigationBarItem(
            label = { Text(text = screen.title) },
            icon = { Icon(imageVector = screen.icon, contentDescription = "Navigation Icon") },
            selected = isSelected,
            // unselectedContentColor = LocalContentColor.current.copy(alpha =
            // ContentAlpha.disabled),
            onClick = { navController.navigate(screen.route) }
    )
}
