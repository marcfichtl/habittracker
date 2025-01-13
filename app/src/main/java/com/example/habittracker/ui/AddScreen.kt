package com.example.habittracker.ui

import android.content.res.Resources.Theme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val colorOptions = listOf(
    Color(0xFFFFFFFF),
)

@Composable
fun AddScreen() {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(colorOptions[0]) }
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFf48b94),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .fillMaxWidth()
                .weight(0.3f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Habit Name")
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(
                            BorderStroke(2.dp, Color.White),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {

        }
    }
}