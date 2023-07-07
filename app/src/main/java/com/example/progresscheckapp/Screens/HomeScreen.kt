package com.example.progresscheckapp.Screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.progresscheckapp.BottomNavigation
import com.example.progresscheckapp.Event
import com.example.progresscheckapp.Screen
import com.example.progresscheckapp.clickEvent
import com.example.progresscheckapp.eventList
import com.example.progresscheckapp.getHan1Zen2




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                if (!eventList.isEmpty()) {
                    var count = 0
                    for (event in eventList) {
                        EventSurface(navController = navController, event = event, count = count)
                        count++
                    }
                } else {
                    Text(
                        text = "イベントがありません",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            ScreenActionButtom(navController = navController, Screen.MakeEvents.screen_route)
        }
    }
}

@Composable
fun ScreenActionButtom(navController: NavHostController, route: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.885f),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = {
                navController.navigate(route)
            }
        ) {
            Icon(
                painterResource(id = Screen.MakeEvents.icon),
                contentDescription = "追加"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventSurface(navController: NavHostController, event: Event, count: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                clickEvent = count
                navController.navigate(Screen.EditEvents.screen_route)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
        ) {
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(65.dp)
                    .background(event.color)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(8.dp)
            ) {
                Text(
                    text = event.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (event.kickOff?.year.toString() == event.codeFreeze?.year.toString()) {
                    Text(
                        text = event.kickOff?.year.toString() + "年" + event.kickOff?.monthValue.toString() + "月" + event.kickOff?.dayOfMonth.toString() + "日"
                                + "～" + event.codeFreeze?.monthValue.toString() + "月" + event.codeFreeze?.dayOfMonth.toString() + "日",
                        fontSize = 12.sp,
                    )
                } else {
                    Text(
                        text = event.kickOff?.year.toString() + "年" + event.kickOff?.monthValue.toString() + "月" + event.kickOff?.dayOfMonth.toString() + "日"
                                + "～" + event.codeFreeze?.year.toString() + "年" + event.codeFreeze?.monthValue.toString() + "月" + event.codeFreeze?.dayOfMonth.toString() + "日",
                        fontSize = 12.sp,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "リーダー",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = " " + event.leader,
                    fontSize = 9.sp,
                )
                Text(
                    text = "メンバー",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row() {
                    var counter = 0
                    for (member in event.members) {
                        counter = counter + getHan1Zen2(member)
                        if (counter > 25) {
                            Text(
                                text = " . . .",
                                fontSize = 9.sp
                            )
                            break
                        }
                        Text(
                            text = " " + member + ",",
                            fontSize = 9.sp,
                        )
                    }
                }
            }
        }
    }
}


