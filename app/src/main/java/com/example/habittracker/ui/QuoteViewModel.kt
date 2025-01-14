package com.example.habittracker.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.InputStreamReader
import kotlin.random.Random

data class Quote(val id: Int, val text: String)

class QuoteViewModel : ViewModel() {
    private var randomQuote by mutableStateOf<Quote?>(null)

    fun loadQuote(context: Context) {
        viewModelScope.launch {
            val quotes = getQuotesFromAssets(context)
            randomQuote = quotes[Random.nextInt(quotes.size)]
        }
    }

    companion object {
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
    }
}