import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import motorbuscaminas.Buscaminas
import kotlinx.coroutines.Job

class EstadoJuego(filas: Int, columnas: Int, minas: Int) {
    private val buscaminas = Buscaminas(filas, columnas, minas)
    val tablero = buscaminas.obtenerTablero()
    var juegoTerminado by mutableStateOf(false)
    var tiempoTranscurrido by mutableStateOf(0)

    fun destapar(fila: Int, columna: Int) {
        if (juegoTerminado) return
        buscaminas.destapar(fila, columna)
        if (buscaminas.juegoTerminado) {
            juegoTerminado = true
        }
    }

    fun reiniciar() {
        buscaminas.reiniciar()
        tiempoTranscurrido = 0
        juegoTerminado = false
    }
}
