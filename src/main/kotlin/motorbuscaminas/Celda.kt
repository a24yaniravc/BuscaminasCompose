package motorbuscaminas

class Celda {
    var estaRevelada = false
    var tieneMina = false
    var minasAdyacentes = 0
    var tieneBandera = false

    //Reinicia valores celda
    fun resetear() {
        estaRevelada = false
        tieneMina = false
        minasAdyacentes = 0
        tieneBandera = false
    }
}

