package com.unilibre.clima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unilibre.clima.data.AppDatabase
import com.unilibre.clima.data.WeatherApi
import com.unilibre.clima.data.WeatherRepository
import com.unilibre.clima.ui.WeatherScreen
import com.unilibre.clima.ui.WeatherViewModel
import com.unilibre.clima.ui.theme.ClimaTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual dependency injection for this workshop
        val api = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
        
        val database = AppDatabase.getDatabase(this)
        val repository = WeatherRepository(api, database.historialDao())
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WeatherViewModel(repository) as T
            }
        }

        enableEdgeToEdge()
        setContent {
            ClimaTheme {
                val viewModel: WeatherViewModel = viewModel(factory = viewModelFactory)
                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}
