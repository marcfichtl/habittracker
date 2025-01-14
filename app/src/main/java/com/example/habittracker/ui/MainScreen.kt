package com.example.habittracker.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import org.json.JSONObject
import java.io.InputStreamReader
import kotlin.random.Random

enum class Screens(val route: String) {
    Main("Main"),
    Add("Add"),
    Edit("edit/{habitId}")
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    dataViewModel: DataViewModel = viewModel(factory = ViewModelFactoryProvider.Factory)
) {
    val navController = rememberNavController()
    val state by dataViewModel.habitsUiState.collectAsStateWithLifecycle()

    NavHost(
        navController, Screens.Main.route,
        modifier = modifier
    ) {
        composable(Screens.Main.route) {
            Column {
                AddButton(navController)
                Quote(context = LocalContext.current, modifier = Modifier.weight(1f))
                HabitItem(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(100.dp)
                )
            }
            //TODO("Display the Habits on a List")
        }
        composable(Screens.Add.route) {
            AddScreen(navController, dataViewModel)
        }
        composable(Screens.Edit.route) {
            //TODO("Edit a Habit")
        }
    }
}

data class Quote(val id: Int, val text: String)

@Composable
fun Quote(context: Context, modifier: Modifier) {
    val quotes = getQuotesFromAssets(context)
    val randomQuote = quotes[Random.nextInt(quotes.size)]
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = randomQuote.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

fun getQuotesFromAssets(context: Context): List<Quote> {
    val inputStream = context.assets.open("quotes.json")
    val reader = InputStreamReader(inputStream)
    val quotesJson = reader.readText()
    val quotesArray = JSONObject(quotesJson).getJSONArray("quotes")
    val quotes = mutableListOf<Quote>()

    for (i in 0 until quotesArray.length()) {
        val quoteObject = quotesArray.getJSONObject(i)
        val id = quoteObject.keys().next().toInt()
        val text = quoteObject.getString(id.toString())
        quotes.add(Quote(id, text))
    }

    return quotes
}

@Composable
fun HabitCard() {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .height(100.dp),
        headlineContent = {
            Text(
                text = "Run 1 km",
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = "16/30 days",
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingContent = {
            Icon(
                painter = painterResource(id = R.drawable.circle),
                contentDescription = "checkmark icon",
                Modifier
                    .clip(CircleShape)
                    .padding(10.dp)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF1DE9B6)
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
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        Icon(
            painter = painterResource(R.drawable.check),
            contentDescription = "check"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitItem(
    //emailMessage: EmailMessage,
    modifier: Modifier = Modifier,
    //onRemove: () -> Unit
) {
    val context = LocalContext.current
    //val currentItem by rememberUpdatedState(emailMessage)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    //onRemove(currentItem)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    //onRemove(currentItem)
                    Toast.makeText(context, "Item archived", Toast.LENGTH_SHORT).show()
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            HabitCard()
        })
}

@Composable
fun AddButton(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { navController.navigate(Screens.Add.route) },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}