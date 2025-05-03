import androidx.compose.runtime.mutableStateOf
import motorbuscaminas.Buscaminas

class EstadoJuego(filas: Int, columnas: Int, minas: Int) {
    private val buscaminas = Buscaminas(filas, columnas, minas)
    val tablero = buscaminas.obtenerTablero()

    var juegoTerminado = mutableStateOf(false)
    var bombaExplotada = mutableStateOf(false)
    var tiempoTranscurrido = mutableStateOf(0)

    fun destapar(fila: Int, columna: Int) {
        if (juegoTerminado.value) return
        val celda = tablero[fila][columna]

        // Si se destapa una bomba, se pierde
        if (celda.tieneMina) {
            bombaExplotada.value = true
            juegoTerminado.value = true
            revelarTodoElTablero()
        } else {
            buscaminas.destapar(fila, columna)

            // Verificar si el jugador ha ganado (todas las celdas sin mina reveladas)
            if (!bombaExplotada.value && todasLasCeldasSegurasReveladas()) {
                juegoTerminado.value = true
            }
        }
    }

    private fun todasLasCeldasSegurasReveladas(): Boolean {
        for (fila in tablero) {
            for (celda in fila) {
                if (!celda.estaRevelada && !celda.tieneMina) {
                    return false
                }
            }
        }
        return true
    }

    private fun revelarTodoElTablero() {
        for (fila in tablero) {
            for (celda in fila) {
                celda.estaRevelada = true // Revelar todas las celdas
            }
        }
    }

    fun reiniciar() {
        buscaminas.reiniciar()          // Reinicia el estado del tablero en la clase Buscaminas
        tiempoTranscurrido.value = 0    // Reinicia el tiempo
        juegoTerminado.value = false    // Restablece la condición de juego terminado
        bombaExplotada.value = false    // Estado bomba explotada se restablece

        for (fila in tablero) {
            for (celda in fila) {
                celda.estaRevelada = false
                celda.tieneBandera = false
            }
        }
    }

    fun colocarBandera(fila: Int, columna: Int) {
        val celda = tablero[fila][columna]
        if (!celda.estaRevelada) {
            celda.tieneBandera = true
        }
    }


    // No se usa
    /*fun quitarBandera(fila: Int, columna: Int) {
        val celda = tablero[fila][columna]
        if (!celda.estaRevelada) {
            celda.tieneBandera = false
        }
    }*/
}
