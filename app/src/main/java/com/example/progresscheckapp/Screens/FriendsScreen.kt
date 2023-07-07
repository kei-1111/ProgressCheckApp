package com.example.progresscheckapp.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.progresscheckapp.BottomNavigation
import com.example.progresscheckapp.Screen
import com.example.progresscheckapp.ShortMyTextField
import com.example.progresscheckapp.getHan1Zen2

@Composable
fun FriendsScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "フレンド一覧",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Divider(color = Color.LightGray)
            }
            DialogActionButtom()
        }
    }
}

@Composable
fun DialogActionButtom() {
    var dialogOpen by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.885f),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = {
                dialogOpen = true
            }
        ) {
            Icon(
                painterResource(id = Screen.MakeEvents.icon),
                contentDescription = "追加"
            )
        }
        if (dialogOpen) AddFriendsDialog(onDialogRequest = { dialogOpen = false })
    }
}


@Composable
fun AddFriendsDialog(onDialogRequest: () -> Unit = {}) {
    val labels = listOf(
        "名前",
        "ID"
    )
    var charCounter = 0
    var name by rememberSaveable { mutableStateOf("") }
    var id by rememberSaveable { mutableStateOf("") }
    for(label in labels){
        if(charCounter<= getHan1Zen2(label)) charCounter = getHan1Zen2(label)
    }
    Dialog(
        onDismissRequest = {
            onDialogRequest()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "フレンド追加",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                ShortMyTextField(
                    label = labels[0],
                    text = name,
                    padding = 10,
                    spacer = charCounter - getHan1Zen2(labels[0]),
                    onValueChange = {
                        if (it.length <= 13) name= it
                    }
                )
                Divider(color = Color.LightGray)
                ShortMyTextField(
                    label = labels[1],
                    text = id,
                    padding = 10,
                    spacer = charCounter - getHan1Zen2(labels[1]),
                    onValueChange = {
                        if (it.length <= 17) id= it
                    }
                )
                Divider(color = Color.LightGray)
            }
        }
    }
}