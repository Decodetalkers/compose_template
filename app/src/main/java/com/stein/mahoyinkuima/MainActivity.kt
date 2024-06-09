package com.stein.mahoyinkuima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.stein.mahoyinkuima.db.ChatHistoryDataBase
import com.stein.mahoyinkuima.db.ChatHistoryModel
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
                        ) { Greeting("Android", viewModel) }
            }
        }
    }
}

@Composable
fun Greeting(name: String, dbModel: ChatHistoryModel) {
    val navController = rememberNavController()

    // NOTE: just hack it now
    val hasKey = dbModel.getKey().collectAsState(initial = listOf("emptykey"))
    MaterialTheme {
        Scaffold(bottomBar = { BottomBar(navController = rememberNavController()) }) { padding ->
            NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
                composable(BottomBarScreen.Home.route) {
                    Text(
                            text = "I love android${name}",
                            modifier = Modifier.fillMaxSize().padding(padding)
                    )
                    if (hasKey.value.isEmpty()) {
                        var id by remember { mutableStateOf(String()) }
                        Dialog(onDismissRequest = {}) {
                            Card(
                                    modifier =
                                            Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
                                    shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                            text = "please input a new id",
                                            modifier =
                                                    Modifier.fillMaxWidth()
                                                            .padding(8.dp)
                                                            .wrapContentSize(Alignment.Center),
                                            textAlign = TextAlign.Center
                                    )
                                    OutlinedTextField(
                                            value = id,
                                            singleLine = true,
                                            shape = shapes.large,
                                            onValueChange = { value -> id = value },
                                            colors =
                                                    TextFieldDefaults.colors(
                                                            focusedContainerColor =
                                                                    colorScheme.surface,
                                                            unfocusedContainerColor =
                                                                    colorScheme.surface,
                                                            disabledContainerColor =
                                                                    colorScheme.surface,
                                                    ),
                                            label = { Text("id") },
                                            isError = false,
                                            keyboardActions =
                                                    KeyboardActions(
                                                            onDone = { dbModel.updateKey(id) }
                                                    )
                                    )
                                }
                            }
                        }
                    }
                }
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
