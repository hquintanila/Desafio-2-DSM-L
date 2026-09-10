package com.example.desafio_2dsm_l.model

data class Viaje(
    var id: String = "",
    var titulo: String = "",
    var descripcion: String = "",
    var precio: Double = 0.0,
    var duracion: String = "",      // Ejemplo: "5 Días / 4 Noches"
    var ubicacion: String = "",     // Ejemplo: "Cancún, México"
    var imagenUrl: String = "",     // URL remota de Firebase Storage o web
    var imagenResId: Int = 0,       // ID del recurso local (Ejemplo: R.drawable.cancun1)
    var disponible: Boolean = true
)