package com.example.desafio_2dsm_l.model

object CartManager {
    private val listaCarrito = mutableListOf<Viaje>()

    fun agregarAlCarrito(viaje: Viaje) {
        listaCarrito.add(viaje)
    }

    fun obtenerCarrito(): List<Viaje> {
        return listaCarrito
    }

    fun limpiarCarrito() {
        listaCarrito.clear()
    }
}