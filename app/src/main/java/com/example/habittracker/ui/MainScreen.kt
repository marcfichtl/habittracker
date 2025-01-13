package com.example.habittracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    dataViewModel: DataViewModel = viewModel(factory = ViewModelFactoryProvider.Factory)
){
    val state by dataViewModel.habitsUiState.collectAsStateWithLifecycle() //

    Button(
        onClick = ({ /*TODO*/ })
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
}