package com.example.habittracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.R
import com.example.habittracker.ui.theme.OutfitFontFamily
import com.example.habittracker.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(onFinish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val images = listOf(R.drawable.tutorial1, R.drawable.tutorial2, R.drawable.tutorial3)
    val tutorialText = listOf(
        "Tap a habit to mark it as done",
        "Swipe right to edit a habit",
        "Swipe left to see statistics of a habit"
    )
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(0.5f))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).align(Alignment.CenterHorizontally)
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = tutorialText[page],
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = OutfitFontFamily,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "tutorial",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (pagerState.currentPage == index) Primary else Color.Gray)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onFinish
            ) {
                Text(
                    "Skip",
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = OutfitFontFamily
                )
            }
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF99188)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent
                ),
                onClick = {
                if(pagerState.currentPage == images.size -1) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }) {
                Text(
                    text = if (pagerState.currentPage == images.size - 1) "Finish" else "Next",
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = OutfitFontFamily
                )
            }
        }
        Spacer(Modifier.weight(0.5f))
    }
}