package com.example.habittracker.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habittracker.data.Habit
import com.example.habittracker.ui.theme.OutfitFontFamily
import com.example.habittracker.ui.theme.Primary
import com.example.habittracker.ui.theme.colorOptions

@Composable
fun AddScreen(navController: NavController, dataviewmodel: DataViewModel) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(colorOptions[0]) }
    var showDialogColor by remember { mutableStateOf(false) }
    var reminderChecked by remember { mutableStateOf(false) }
    var showDialogRepeat by remember { mutableStateOf(false) }
    var selectedRepeat by remember { mutableStateOf("every day") }
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = selectedColor,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .fillMaxWidth()
                .weight(0.3f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Habit Name",
                    color = Primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= 40) {
                            name = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .border(
                            BorderStroke(2.dp, Primary),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    textStyle = TextStyle(
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        fontFamily = OutfitFontFamily
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Primary,
                        unfocusedTextColor = Primary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
            }
        }

        // Under colored box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(top = 72.dp, start = 32.dp, end = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialogRepeat = true }
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Repeat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Row {
                    Text(
                        text = selectedRepeat,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Select Repeat",
                        tint = Primary
                    )
                }

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose Color",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(50))
                        .background(selectedColor)
                        .clickable { showDialogColor = true }
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reminder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Checkbox(
                    modifier = Modifier.scale(1.5f),
                    checked = reminderChecked,
                    onCheckedChange = { checked ->
                        reminderChecked = checked
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = Primary,
                        checkmarkColor = Primary,
                        checkedColor = selectedColor
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp, start = 32.dp, end = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = OutfitFontFamily
                    )
                }
                Button(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(selectedColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    ),
                    onClick = {
                        if (name.isEmpty()) {
                            val errorToast = Toast.makeText(
                                context,
                                "Please enter a habit name",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            dataviewmodel.onAddHabitClick(

                                Habit(
                                    id = 0,
                                    name = name,
                                    color = colorOptions.indexOf(selectedColor),
                                    reminder = reminderChecked,
                                    finished = emptyList(),
                                    repeat = when (selectedRepeat) {
                                        "every day" -> 0
                                        "sunday" -> 1
                                        "monday" -> 2
                                        "tuesday" -> 3
                                        "wednesday" -> 4
                                        "thursday" -> 5
                                        "friday" -> 6
                                        "saturday" -> 7
                                        else -> 0
                                    }
                                )

                            )
                            navController.popBackStack()
                        }

                    },
                ) {
                    Text(
                        "Save",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = OutfitFontFamily
                    )
                }
            }
        }

        if (showDialogColor) {
            AlertDialog(
                onDismissRequest = { showDialogColor = false },
                title = { Text("Choose Color") },
                text = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(colorOptions.size) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(colorOptions[index])
                                    .clickable {
                                        selectedColor = colorOptions[index]
                                        showDialogColor = false
                                    },
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialogColor = false }) {
                        Text("Close")
                    }
                }
            )
        }

        if (showDialogRepeat) {
            val days = listOf(
                "every day",
                "monday",
                "tuesday",
                "wednesday",
                "thursday",
                "friday",
                "saturday",
                "sunday"
            )

            AlertDialog(
                onDismissRequest = { showDialogRepeat = false },
                title = { Text("Repeat on") },
                text = {
                    LazyColumn {
                        items(days) { day ->
                            Text(
                                text = day,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedRepeat = day
                                        showDialogRepeat = false
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialogRepeat = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}