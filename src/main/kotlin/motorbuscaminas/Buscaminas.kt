package motorbuscaminas

import kotlin.random.Random

class Buscaminas(private val filas: Int, private val columnas: Int, private val minas: Int) {
    private val tablero = Array(filas) { Array(columnas) { Celda() } }

    var juegoTerminado = false
        private set
    private var celdasRestantes = filas * columnas - minas

    init {
        if (filas <= 1 || columnas <= 1) throw Exception("Tanto el número de filas como el de columnas deben ser mayor que 1.")
        if (minas >= filas * columnas) throw Exception("El número de minas no puede ser mayor o igual que el número de celdas.")
        colocarMinas()
        calcularMinasAdyacentes()
    }

    private fun colocarMinas() {
        var minasColocadas = 0
        while (minasColocadas < minas) {
            val fila = Random.nextInt(filas)
            val columna = Random.nextInt(columnas)

            if (!tablero[fila][columna].tieneMina) {
                tablero[fila][columna].tieneMina = true
                minasColocadas++
            }
        }
    }

    private fun calcularMinasAdyacentes() {
        for (fila in 0 until filas) {
            for (columna in 0 until columnas) {
                if (!tablero[fila][columna].tieneMina) {
                    val minasAdyacentes = contarMinasAdyacentes(fila, columna)
                    tablero[fila][columna].minasAdyacentes = minasAdyacentes
                }
            }
        }
    }

    private fun contarMinasAdyacentes(fila: Int, columna: Int): Int {
        var contador = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val nuevaFila = fila + i
                val nuevaColumna = columna + j
                if (nuevaFila in 0 until filas &&
                    nuevaColumna in 0 until columnas &&
                    tablero[nuevaFila][nuevaColumna].tieneMina
                ) {
                    contador++
                }
            }
        }
        return contador
    }

    fun destapar(fila: Int, columna: Int) {
        if (fila !in 0 until filas || columna !in 0 until columnas) return
        val celda = tablero[fila][columna]

        if (celda.estaRevelada || celda.tieneBandera || juegoTerminado) return

        celda.estaRevelada = true

        if (celda.tieneMina) {
            juegoTerminado = true
        } else {
            celdasRestantes--
            if (celda.minasAdyacentes == 0) {
                for (i in -1..1) {
                    for (j in -1..1) {
                        if (i != 0 || j != 0) {
                            destapar(fila + i, columna + j)
                        }
                    }
                }
            }
            if (celdasRestantes == 0) {
                juegoTerminado = true
            }
        }
    }

    fun obtenerTablero(): Array<Array<Celda>> = tablero

    fun reiniciar() {
        for (i in 0 until filas) {
            for (j in 0 until columnas) {
                tablero[i][j].resetear()
            }
        }
        colocarMinas()
        calcularMinasAdyacentes()
        juegoTerminado = false
        celdasRestantes = filas * columnas - minas
    }
}
