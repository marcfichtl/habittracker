package com.example.habittracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.habittracker.ui.theme.OutfitFontFamily
import com.example.habittracker.ui.theme.Primary
import com.example.habittracker.ui.theme.colorOptions

@Composable
fun DayOverviewScreen(date: String, dataViewModel: DataViewModel, navController: NavController) {
    val habits by dataViewModel.getFinishedHabitsByDate(date).collectAsStateWithLifecycle(emptyList())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Habits finished on ${date}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        LazyColumn {
            items(habits) { habit ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(colorOptions[habit.color], shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = habit.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
        TextButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(
                text = "Back",
                color = Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = OutfitFontFamily
            )
        }
    }
}