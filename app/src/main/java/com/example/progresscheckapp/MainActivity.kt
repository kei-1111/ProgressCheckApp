package com.example.progresscheckapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.progresscheckapp.Screens.AuthScreen
import com.example.progresscheckapp.Screens.AuthViewModel
import com.example.progresscheckapp.Screens.CalendarScreen
import com.example.progresscheckapp.Screens.EditEventsScreen
import com.example.progresscheckapp.Screens.EditSettingScreen
import com.example.progresscheckapp.Screens.FriendsScreen
import com.example.progresscheckapp.Screens.HomeScreen
import com.example.progresscheckapp.Screens.MakeEventsScreen
import com.example.progresscheckapp.Screens.ProfileViewModel
import com.example.progresscheckapp.Screens.SettingScreen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate


@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<AuthViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberAnimatedNavController()
            NavigationGraph(
                navController = navController
            )
            checkAuthState()
        }
    }
    private fun checkAuthState() {
        if(viewModel.isUserAuthenticated) {
            navigateToHomeScreen()
        }
    }

    private fun navigateToHomeScreen() = navController.navigate(Screen.Home.screen_route)
}

/*
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

        }
    }
}
*/

data class Event(
    var title: String,
    var kickOff: LocalDate?,
    var codeFreeze: LocalDate?,
    var color: Color,
    val leader: String,
    var members: List<String>
) {
    fun isEmpty(): Boolean {
        if (title == "") return true
        if (kickOff == null) return true
        if (codeFreeze == null) return true
        if (color == Color.Unspecified) return true
        if (leader == "") return true
        if (members == listOf<String>()) return true
        return false
    }
}

data class User(
    var icon: Int,
    var name: String,
    var joinEvent: List<Event>,
    var id: String,
    var selfIntroduction: String,
)

val database = FirebaseDatabase.getInstance()
var eventList = mutableListOf<Event>()
var clickEvent = 0
var myAcount = User(
    icon = R.drawable.my_icon_coco,
    name = "名無し",
    joinEvent = eventList,
    id = "",
    selfIntroduction = "よろしくおねがいします。"
)

@Composable
fun SetUpUser(
    viewModel: ProfileViewModel = hiltViewModel(),
) :User{
    var user = User(
        icon = R.drawable.my_icon_coco,
        name = myAcount.name,
        joinEvent = eventList,
        id = viewModel.email,
        selfIntroduction = myAcount.selfIntroduction
    )
    return user
}


sealed class Screen(var title: String, var icon: Int, var screen_route: String) {

    object Auth : Screen("ログイン", 0, "auth")
    object Home : Screen("ホーム", R.drawable.ic_home, "home")
    object Calendar : Screen("カレンダー", R.drawable.ic_calendar, "calendar")
    object Friends : Screen("フレンド", R.drawable.ic_friends, "friends")
    object Setting : Screen("設定", R.drawable.ic_setting, "setting")
    object EditSetting : Screen("", 0, "edit_setting")
    object MakeEvents : Screen("", R.drawable.ic_add, "make_events")
    object EditEvents : Screen("", 0, "edit_events")

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    NavHost(navController, startDestination = Screen.Auth.screen_route) {
        composable(Screen.Auth.screen_route) {
            AuthScreen(
                navigateToHomeScreen = { navController.navigate(Screen.Home.screen_route) }
            )
        }
        composable(Screen.Home.screen_route) {
            myAcount = SetUpUser()
            HomeScreen(
                navController = navController
            )
        }
        composable(Screen.Calendar.screen_route) {
            CalendarScreen(
                navController = navController
            )
        }
        composable(Screen.Friends.screen_route) {
            FriendsScreen(
                navController = navController
            )
        }
        composable(Screen.Setting.screen_route) {
            SettingScreen(
                signOut = {
                    viewModel.signOut()
                },
                navController = navController
            )
        }
        composable(Screen.EditSetting.screen_route) {
            EditSettingScreen(
                navController = navController
            )
        }
        composable(Screen.MakeEvents.screen_route) {
            MakeEventsScreen(
                navController = navController
            )
        }
        composable(Screen.EditEvents.screen_route) {
            EditEventsScreen(
                navController = navController,
                event = eventList[clickEvent]
            )
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Calendar,
        Screen.Friends,
        Screen.Setting,
    )
    BottomAppBar(
        modifier = Modifier
            .height(60.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(alpha = 0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/*
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenView() {
    var navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

 */