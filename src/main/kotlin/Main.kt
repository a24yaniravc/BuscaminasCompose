import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
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
    val estadoJuego = remember { EstadoJuego(8, 8, 10) }

    // Cronómetro
    LaunchedEffect(estadoJuego.contadorReinicio.value) {
        while (!estadoJuego.juegoTerminado.value) {
            delay(1000L)
            estadoJuego.tiempoTranscurrido.value++
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Tiempo: ${estadoJuego.tiempoTranscurrido.value} segundos")

        Spacer(modifier = Modifier.height(8.dp))

        // Ganar / Perder
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
                                        // Colocar/Quitar bandera con pulsado largo
                                        if (!celda.tieneBandera) {
                                            estadoJuego.colocarBandera(filaIndex, columnaIndex)
                                        } else {
                                            estadoJuego.quitarBandera(filaIndex, columnaIndex)
                                        }
                                    },
                                    onTap = {
                                        // Si no tiene bandera y se hace un pulsado corto, destapa celda
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
