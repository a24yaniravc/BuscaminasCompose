import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@Composable
fun BuscaminasUI() {
    // Estado del juego
    val estadoJuego = remember { EstadoJuego(8, 8, 10) }

    // Cronómetro
    LaunchedEffect(estadoJuego) {
        while (!estadoJuego.juegoTerminado) {
            delay(1000L)
            if (!estadoJuego.juegoTerminado) {
                estadoJuego.tiempoTranscurrido++
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Tiempo: ${estadoJuego.tiempoTranscurrido} segundos")
        Spacer(modifier = Modifier.height(8.dp))

        estadoJuego.tablero.forEachIndexed { filaIndex, fila ->
            Row {
                fila.forEachIndexed { columnaIndex, celda ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                when {
                                    celda.estaRevelada -> Color.LightGray
                                    celda.tieneBandera -> Color.Yellow
                                    else -> Color.DarkGray
                                }
                            )
                            .clickable {
                                if (!celda.estaRevelada) {
                                    estadoJuego.destapar(filaIndex, columnaIndex)
                                }
                            }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (celda.estaRevelada) {
                            Text(if (celda.tieneMina) "💣" else "${celda.minasAdyacentes}")
                        } else if (celda.tieneBandera) {
                            Text("🚩")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Button para reiniciar el juego
        Button(onClick = { estadoJuego.reiniciar() }) {
            Text("Reiniciar")
        }
    }
}

@Preview
@Composable
fun App() {
    MaterialTheme {
        BuscaminasUI()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Buscaminas") {
        App()
    }
}
