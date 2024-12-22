package com.stein.mahoyinkuima

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stein.mahoyinkuima.common.Resource
import com.stein.mahoyinkuima.nhk.NhKViewModel
import com.stein.mahoyinkuima.nhk.NhkHtmlModel
import com.stein.mahoyinkuima.nhk.newsHtml
import com.stein.mahoyinkuima.nhk.newsView
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
                        ) { OverAllView() }
            }
        }
    }
}

@Composable
fun OverAllView() {
    val nhkhtml: NhkHtmlModel = viewModel()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = OverViewScreen.Main.route) {
        composable(OverViewScreen.Main.route) { MainView(nhkhtml, navController) }
        composable(OverViewScreen.News.route) { WebViewScreen(nhkhtml) }
    }
}

@Composable
fun MainView(htmlModel: NhkHtmlModel, upNavController: NavHostController) {
    val navController = rememberNavController()
    val nhkView: NhKViewModel = viewModel()
    nhkView.syncNews()

    // NOTE: just hack it now
    MaterialTheme {
        Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
            NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
                composable(BottomBarScreen.Home.route) {
                    NhkPackage(
                            model = nhkView,
                            htmlModel = htmlModel,
                            upNavController = upNavController,
                            dp = padding
                    )
                }

                composable(BottomBarScreen.Settings.route) {
                    Column(
                            modifier = Modifier.padding(padding).fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                    ) { Text(text = "A Phone Info shown app", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
fun NhkPackage(
        model: NhKViewModel,
        htmlModel: NhkHtmlModel,
        upNavController: NavHostController,
        dp: PaddingValues? = null
) {
    val state by model.state
    val glModifier =
            Modifier.fillMaxSize().let done@{
                if (dp == null) return@done it
                it.padding(dp)
            }
    when (val smartCastData = state) {
        is Resource.Success ->
                LazyColumn(modifier = glModifier) {
                    items(smartCastData.data) { data ->
                        data.newsView {
                            htmlModel.setData(data.newsHtml())

                            upNavController.navigate(OverViewScreen.News.route)
                        }
                    }
                }
        else ->
                Column(
                        modifier = glModifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) { Text(text = "Loading....", fontWeight = FontWeight.Bold) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MahoyinkuimaTheme { /*Greeting("Android") */}
}

@Composable
fun WebViewScreen(nhkhtml: NhkHtmlModel) {
    val HTMLstring = nhkhtml.htmlData.value
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
                factory = {
                    WebView(it).apply {
                        layoutParams =
                                ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                )
                    }
                },
                update = { it.loadDataWithBaseURL(null, HTMLstring, "text/html", "utf-8", null) }
        )
    }
}

@Composable
private fun BottomBar(
        navController: NavHostController,
) {
    var selectedDestination by remember { mutableIntStateOf(0) }

    val screens = listOf(BottomBarScreen.Home, BottomBarScreen.Settings)

    val callback =
            NavController.OnDestinationChangedListener end@{ _, destination, _ ->
                if (destination.route == null) return@end
                val index = screens.withIndex().first { destination.route == it.value.route }.index
                if (index >= 0) selectedDestination = index
            }
    navController.addOnDestinationChangedListener(callback)
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            AddItem(
                    screen = screen,
                    isSelected = index == selectedDestination,
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
            onClick = { navController.navigate(screen.route) }
    )
}
