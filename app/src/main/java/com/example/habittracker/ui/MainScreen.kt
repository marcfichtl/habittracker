package com.example.habittracker.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracker.R
import com.example.habittracker.data.Habit
import com.example.habittracker.ui.theme.Background
import com.example.habittracker.ui.theme.Primary
import com.example.habittracker.ui.theme.colorOptions
import org.json.JSONObject
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class Screens(val route: String) {
    Main("Main"),
    Add("Add"),
    Edit("edit/{habitId}"),
    Stats("stats/{habitId}")
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    randomQuote: Quote,
    dataViewModel: DataViewModel = viewModel(factory = ViewModelFactoryProvider.Factory)
) {
    val navController = rememberNavController()
    val state by dataViewModel.habitsUiState.collectAsStateWithLifecycle()

    NavHost(
        navController, Screens.Main.route, modifier = modifier
    ) {
        composable(Screens.Main.route) {
            Column {
                AddButton(navController)
                QuoteDisplay(randomQuote, modifier = Modifier.weight(1f))

                LazyColumn {
                    items(state.habits) { habit ->
                        HabitItem(
                            habit = habit,
                            modifier = Modifier
                                .padding(16.dp)
                                .height(100.dp),
                            navController,
                            dataViewModel
                        )
                    }
                }
            }
        }
        composable(Screens.Add.route) {
            AddScreen(navController, dataViewModel)
        }
        composable("Edit/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toInt() ?: 0
            EditScreen(navController, dataViewModel, habitId)
        }
        composable(Screens.Stats.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toInt() ?: 0
            StatsScreen(navController, dataViewModel, habitId)
        }
    }
}

@Composable
fun QuoteDisplay(quote: Quote, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = quote.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Primary
        )
    }
}

@Composable
fun HabitCard(habit: Habit, navController: NavController) {
    val today = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val todayString = dateFormat.format(today)
    val calendar = Calendar.getInstance()
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val daysInMonthWithRepeatCalculated = (1..daysInMonth).count { day ->
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.YEAR, currentYear)
        }
        habit.repeat == 0 || calendar.get(Calendar.DAY_OF_WEEK) == habit.repeat
    }

    val daysCompletedInMonth = habit.finished.count {
        val date = dateFormat.parse(dateFormat.format(it))
        date?.let {
            val habitCalendar = Calendar.getInstance().apply { time = it }
            habitCalendar.get(Calendar.MONTH) == currentMonth && habitCalendar.get(Calendar.YEAR) == currentYear &&
                    (habit.repeat == 0 || habitCalendar.get(Calendar.DAY_OF_WEEK) == habit.repeat)
        } ?: false
    }

    val progress by animateFloatAsState(
        targetValue = if (habit.repeat == 0) {
            daysCompletedInMonth.toFloat() / daysInMonth //progress for daily habits, repeat == 0
        } else {
            daysCompletedInMonth.toFloat() / daysInMonthWithRepeatCalculated
        },
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .height(100.dp)
            .background(Color.DarkGray)
            .clickable {
                navController.navigate("stats/${habit.id}")
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(colorOptions[habit.color])
        )

        ListItem(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp)),
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
            headlineContent = {
                Text(
                    text = habit.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            },
            supportingContent = {
                Text(
                    text = "$daysCompletedInMonth/$daysInMonthWithRepeatCalculated Days",
                    fontSize = 14.sp,
                    color = Primary
                )
            },
            trailingContent = {
                if (habit.finished.any { dateFormat.format(it) == todayString }) {
                    Icon(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "checkmark icon",
                        tint = Primary,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(10.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.circle),
                        contentDescription = "checkmark icon",
                        tint = Primary,
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(10.dp)
                    )
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState, isFinishedToday: Boolean) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFF8F8F8)
        SwipeToDismissBoxValue.EndToStart -> Color(
            if (isFinishedToday)
                0xFFff6666
            else 0xFF33cc33
        )

        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Edit, contentDescription = "edit", tint = Background
        )
        Spacer(modifier = Modifier)
        if (isFinishedToday) {
            Icon(
                Icons.Default.Clear, contentDescription = "clear", tint = Background
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.check),
                contentDescription = "check",
                tint = Background
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitItem(
    habit: Habit,
    modifier: Modifier = Modifier,
    navController: NavController,
    dataViewModel: DataViewModel,
    //onHabitChecked: () -> Unit
) {
    val context = LocalContext.current
    val today = remember { Date() }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val todayString = remember { dateFormat.format(today) }
    var isFinishedToday by remember { mutableStateOf(habit.finished.any { dateFormat.format(it) == todayString }) }

    LaunchedEffect(habit.finished) {
        isFinishedToday = habit.finished.any { dateFormat.format(it) == todayString }
    }

    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
        when (it) {
            SwipeToDismissBoxValue.StartToEnd -> {
                navController.navigate("Edit/${habit.id}")
                return@rememberSwipeToDismissBoxState false
            }

            SwipeToDismissBoxValue.EndToStart -> {
                if (isFinishedToday) {
                    dataViewModel.unmarkHabitAsFinished(habit.id)
                    Toast.makeText(context, "Habit marked as unfinished", Toast.LENGTH_SHORT).show()
                } else if (habit.repeat == 0 || Calendar.getInstance()
                        .get(Calendar.DAY_OF_WEEK) == habit.repeat
                ) {
                    dataViewModel.markHabitsAsFinished(habit.id)
                    Toast.makeText(context, "Habit marked as finished", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context, "Habit is set for repeating on ${
                            when (habit.repeat) {
                                1 -> "Sunday"
                                2 -> "Monday"
                                3 -> "Tuesday"
                                4 -> "Wednesday"
                                5 -> "Thursday"
                                6 -> "Friday"
                                7 -> "Saturday"
                                else -> "Unknown"

                            }
                        }", Toast.LENGTH_SHORT
                    ).show()
                }
                return@rememberSwipeToDismissBoxState false
            }

            SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
        }
        //return@rememberSwipeToDismissBoxState true
    },
        // positional threshold of 25%
        positionalThreshold = { it * .25f })
    SwipeToDismissBox(state = dismissState,
        modifier = modifier,
        backgroundContent = { DismissBackground(dismissState, isFinishedToday) },
        content = {
            HabitCard(habit, navController)
        })
}

@Composable
fun AddButton(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { navController.navigate(Screens.Add.route) },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add",
                tint = Primary
            )
        }
    }
}