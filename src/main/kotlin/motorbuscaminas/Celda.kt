package motorbuscaminas

// Permite que Compose observe las Celdas
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Celda {
    var estaRevelada by mutableStateOf(false)
    var tieneMina by mutableStateOf(false)
    var minasAdyacentes by mutableStateOf(0)
    var tieneBandera by mutableStateOf(false)

    fun resetear() {
        estaRevelada = false
        tieneMina = false
        minasAdyacentes = 0
        tieneBandera = false
    }
}
