package com.stein.mahoyinkuima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.stein.mahoyinkuima.db.ChatHistoryDataBase
import com.stein.mahoyinkuima.db.ChatHistoryModel
import com.stein.mahoyinkuima.file.PhoneInfoModel
import com.stein.mahoyinkuima.ui.theme.MahoyinkuimaTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(applicationContext, ChatHistoryDataBase::class.java, "chate.db")
                .build()
    }
    @Suppress("UNCHECKED_CAST")
    private val viewModel by
            viewModels<ChatHistoryModel>(
                    factoryProducer = {
                        object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return ChatHistoryModel(db.chatHistory()) as T
                            }
                        }
                    }
            )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MahoyinkuimaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        // color = MaterialTheme.colorScheme.background
                        ) { Greeting(viewModel) }
            }
        }
    }
}

@Composable
fun Greeting(dbModel: ChatHistoryModel) {
    val navController = rememberNavController()
    val phoneModel: PhoneInfoModel = viewModel()
    phoneModel.load()

    // NOTE: just hack it now
    MaterialTheme {
        Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
            NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
                composable(BottomBarScreen.Home.route) { Text("aa") }

                composable(BottomBarScreen.Profile.route) { Text("aa") }
                composable(BottomBarScreen.Settings.route) { Text("aa") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MahoyinkuimaTheme { /*Greeting("Android") */}
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
