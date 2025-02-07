package com.example.habittracker.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.habittracker.ui.theme.Primary
import java.util.Calendar

enum class Screens(val route: String) {
    Main("Main"),
    Add("Add"),
    Edit("edit/{habitId}"),
    Stats("stats/{habitId}"),
    DayOverview("day_overview/{formattedDate}")
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    randomQuote: Quote,
    dataViewModel: DataViewModel = viewModel(factory = ViewModelFactoryProvider.Factory)
) {
    val navController = rememberNavController()
    val state by dataViewModel.habitsUiState.collectAsStateWithLifecycle()

    // Default transition animations
    fun defaultTransition() = scaleIn(
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    ) + fadeIn(animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing))

    fun defaultExitTransition() = scaleOut(
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    ) + fadeOut(animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing))

    // Navigation
    NavHost(
        navController, Screens.Main.route, modifier = modifier,
        enterTransition = { defaultTransition() },
        exitTransition = { defaultExitTransition() },
        popEnterTransition = { defaultTransition() },
        popExitTransition = { defaultExitTransition() }
    ) {
        //Main screen
        composable(Screens.Main.route) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val calendar = Calendar.getInstance()
                    Text( //Current month text
                        text = calendar.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            java.util.Locale.US
                        ) ?: "Today",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically),
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                    AddButton(navController)
                }
                QuoteDisplay(randomQuote, modifier = Modifier.weight(1f)) //Random quote

                LazyColumn { //List of habits with HabitItem composable
                    items(state.habits) { habit ->
                        HabitItem(
                            habit = habit,
                            modifier = Modifier
                                .padding(8.dp)
                                .height(100.dp),
                            navController,
                            dataViewModel
                        )
                    }
                }
            }
        }
        //Add screen
        composable(Screens.Add.route) {
            AddScreen(navController, dataViewModel)
        }
        //Edit screen
        composable("Edit/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toInt() ?: 0
            EditScreen(navController, dataViewModel, habitId)
        }
        //Stats screen
        composable(Screens.Stats.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toInt() ?: 0
            StatsScreen(navController, dataViewModel, habitId)
        }
        //Day overview screen
        composable(Screens.DayOverview.route) { backStackEntry ->
            val formattedDate = backStackEntry.arguments?.getString("formattedDate") ?: ""
            DayOverviewScreen(formattedDate, dataViewModel, navController)
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
fun AddButton(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { navController.navigate(Screens.Add.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add",
                tint = Primary
            )
        }
    }
}