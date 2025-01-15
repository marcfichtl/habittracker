package com.example.habittracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.habittracker.R
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(onFinish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val images = listOf(R.drawable.tutorial1, R.drawable.tutorial2, R.drawable.tutorial3)
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "tutorial",
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onFinish) {
                Text("Skip")
            }
            Button(onClick = {
                if(pagerState.currentPage == images.size -1) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }) {
                Text(if (pagerState.currentPage == images.size - 1) "Finish" else "Next")
            }
        }
    }
}