package com.example.desafio_2dsm_l.model

data class Viaje(
    var id: String = "",
    var titulo: String = "",
    var descripcion: String = "",
    var precio: String = "",        // Cambiado a String para coincidir con Firestore
    var duracion: String = "",      // Ejemplo: "5 Días / 4 Noches"
    var ubicacion: String = "",     // Ejemplo: "Cancún, México"
    var imagenUrl: String = "",     // URL remota o nombre del drawable ("alaska", "cartagena", etc.)
    var imagenResId: Int = 0,       // ID del recurso local (Ejemplo: R.drawable.cancun1)
    var disponible: Boolean = true
)