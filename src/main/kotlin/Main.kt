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
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun BuscaminasUI() {
    // Estado del juego
    val estadoJuego = remember { EstadoJuego(8, 8, 10) }

    // Cronómetro
    LaunchedEffect(estadoJuego) {
        while (estadoJuego.juegoTerminado.value == false) {
            delay(1000L)
            if (!estadoJuego.juegoTerminado.value) {
                estadoJuego.tiempoTranscurrido.value++
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Tiempo: ${estadoJuego.tiempoTranscurrido.value} segundos")
        Spacer(modifier = Modifier.height(8.dp))

        // Si el juego ha terminado, mostrar el mensaje de victoria o derrota
        if (estadoJuego.juegoTerminado.value) {
            Text(
                text = if (estadoJuego.bombaExplotada.value) "¡Has perdido! 💣" else "¡Has ganado! 🎉",
                color = if (estadoJuego.bombaExplotada.value) Color.Red else Color.Green,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        estadoJuego.tablero.forEachIndexed { filaIndex, fila ->
            Row {
                fila.forEachIndexed { columnaIndex, celda ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                when {
                                    celda.estaRevelada -> {
                                        if (celda.tieneMina && estadoJuego.bombaExplotada.value) Color.Red else Color.LightGray
                                    }
                                    celda.tieneBandera -> Color.Yellow
                                    else -> Color.DarkGray
                                }
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        // Para clics largos (coloca o quita la bandera)
                                        estadoJuego.colocarBandera(filaIndex, columnaIndex)
                                    },
                                    onTap = {
                                        // Para clics cortos (destapa la celda)
                                        if (!celda.tieneBandera) {
                                            estadoJuego.destapar(filaIndex, columnaIndex)
                                        }
                                    }
                                )
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
