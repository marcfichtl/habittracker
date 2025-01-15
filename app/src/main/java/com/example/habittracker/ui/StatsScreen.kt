package com.example.habittracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.habittracker.ui.theme.Primary
import com.example.habittracker.ui.theme.colorOptions
import java.util.Calendar
import java.util.Date

@Composable
fun StatsScreen(navController: NavController, dataViewModel: DataViewModel, habitId: Int) {
    val habit by dataViewModel.getHabitById(habitId)
        .collectAsStateWithLifecycle(initialValue = null)

    habit?.let { nonNullHabit ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        val finishedDays: List<Date> = nonNullHabit.finished
        val selectedColor by remember { mutableStateOf(colorOptions[nonNullHabit.color]) }
        val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")

        val repeatingDayCount = (1..daysInMonth).count { day ->
            val dayCalendar = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, day)
            }
            nonNullHabit.repeat == 0 || dayCalendar.get(Calendar.DAY_OF_WEEK) == nonNullHabit.repeat
        }

        Column {
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
                Column {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = nonNullHabit.name,
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp),
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                    )
                    Text(
                        text = "You achieved your goal on ${finishedDays.size} out of $repeatingDayCount days this month",
                        modifier = Modifier
                            .padding(24.dp),
                        color = Primary,
                        fontSize = 18.sp,
                    )
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .padding(top = 72.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = calendar.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        java.util.Locale.US
                    ) ?: "Month",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp, start = 8.dp),
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    dayLabels.forEach { label ->
                        Text(
                            text = label,
                            modifier = Modifier.weight(1f),
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    items(startDayOfWeek) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(40.dp)
                        )
                    }
                    items(daysInMonth) { day ->
                        var dayColor = Color.DarkGray
                        var textColor = Color.Gray
                        val dayCalendar = Calendar.getInstance().apply {
                            set(Calendar.DAY_OF_MONTH, day + 1)
                        }
                        if (finishedDays.any { date ->
                                val finishedCalendar = Calendar.getInstance().apply { time = date }
                                finishedCalendar.get(Calendar.DAY_OF_MONTH) == day + 1 &&
                                finishedCalendar.get(Calendar.MONTH) == dayCalendar.get(Calendar.MONTH) &&
                                finishedCalendar.get(Calendar.YEAR) == dayCalendar.get(Calendar.YEAR)
                            }) {
                            dayColor = colorOptions[nonNullHabit.color]
                            textColor = Primary
                        }


                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(40.dp)
                                .background(dayColor, shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = (day + 1).toString(),
                                modifier = Modifier.align(Alignment.Center),
                                color = textColor
                            )
                        }
                    }
                }
            }
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    "Back",
                    color = Primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}