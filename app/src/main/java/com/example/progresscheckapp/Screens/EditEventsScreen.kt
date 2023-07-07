package com.example.progresscheckapp.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.progresscheckapp.Event
import com.example.progresscheckapp.Name
import com.example.progresscheckapp.NameList
import com.example.progresscheckapp.Screen
import com.example.progresscheckapp.SelectColor
import com.example.progresscheckapp.SelectDate
import com.example.progresscheckapp.ShortMyTextField
import com.example.progresscheckapp.clickEvent
import com.example.progresscheckapp.eventList
import com.example.progresscheckapp.getHan1Zen2

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditEventsScreen(navController: NavController,event: Event) {
    val labels = listOf(
        "タイトル",
        "キックオフ",
        "コードフリーズ",
        "カラー",
        "リーダー",
        "参加メンバー"
    )
    var title by remember { mutableStateOf(event.title) }
    var color by remember { mutableStateOf(event.color) }
    var charCounter = 0
    for (label in labels) {
        if (charCounter <= getHan1Zen2(label)) charCounter = getHan1Zen2(label)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EventSetBar(navController = navController, route = Screen.Home.screen_route, event)
        Text(
            text = "ハッカソン編集",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Divider(color = Color.LightGray)
        ShortMyTextField(
            label = labels[0],
            text = title,
            spacer = charCounter - getHan1Zen2(labels[0]),
            onValueChange = {
                if(getHan1Zen2(it) <= 14) title = it
            }
        )
        event.title = title
        SelectDate(
            label = labels[1],
            spacer = charCounter - getHan1Zen2(labels[1]),
            localDate = event.kickOff,
            onValueChange = {
                event.kickOff = it
            }
        )
        SelectDate(
            label = labels[2],
            spacer = charCounter - getHan1Zen2(labels[2]),
            localDate = event.codeFreeze,
            onValueChange = {
                event.codeFreeze = it
            }
        )
        event.color = color
        SelectColor(
            label = labels[3],
            spacer = charCounter - getHan1Zen2(labels[3]),
            color = event.color,
            onValueChange = {
                color = it
            }
        )
        Name(
            label = labels[4],
            spacer = charCounter - getHan1Zen2(labels[4]),
            name = event.leader,
        )
        NameList(
            list = event.members,
            label = labels[5],
            spacer = charCounter - getHan1Zen2(labels[5]),
        )
    }
}

// 編集したものを保存するためのボタン
@Composable
fun EventSetBar(
    navController: NavController,
    route: String,
    event: Event
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    navController.navigate(route)
                }
            ) {
                Text(
                    text = "キャンセル",
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    navController.navigate(route)
                    if (!event.isEmpty()) {
                        eventList.set(clickEvent, event)
                    } else {
                        return@TextButton
                    }

                }
            ) {
                Text(
                    text = "保存",
                    fontSize = 16.sp
                )
            }
        }
        Divider(color = Color.LightGray)
    }
}