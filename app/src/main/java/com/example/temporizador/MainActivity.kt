package com.example.temporizador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BarraNavegacion(navController, "greeting")
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "greeting",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("greeting") {
                        Greeting(
                            name = "user",
                            navController = navController
                        )
                    }
                    composable("Temporizador") {
                        Temporizador()
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String,  navController: NavHostController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Hello $name!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("Temporizador") }) {
                    Text(text = "Ir a Temporizador")
                }
            }
        }
    }

    @Composable
    fun Temporizador(

    ) {
        var time by remember { mutableLongStateOf(0L) }
        var isRunning by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = formatoTiempo(time), modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (isRunning) {
                            isRunning = false
                        } else {
                            isRunning = true
                            coroutineScope.launch {
                                while (isRunning) {
                                    delay(1000L)
                                    time += 1000L
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) Color.Red else Color.Green
                    )
                ) {
                    Text(text = if (isRunning) "Pausar" else "Iniciar")
                }

                Button(
                    onClick = {
                        isRunning = false
                        time = 0L
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(text = "Reiniciar")
                }
            }
        }
    }

    @Composable
    fun formatoTiempo(timeMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    @Composable
    fun BarraNavegacion(navController: NavController, pantallaActual: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                enabled = pantallaActual != "greeting",
                onClick = { navController.navigate("greeting") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Página Principal")
            }
            Button(
                enabled = pantallaActual != "Temporizador",
                onClick = { navController.navigate("Temporizador") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Temporizador")
            }
        }
    }
}
