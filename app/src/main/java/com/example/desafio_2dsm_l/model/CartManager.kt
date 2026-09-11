package com.example.desafio_2dsm_l.model

object CartManager {
    private val listaCarrito = mutableListOf<Viaje>()

    fun agregarAlCarrito(viaje: Viaje) {
        listaCarrito.add(viaje)
    }

    fun obtenerCarrito(): List<Viaje> {
        return listaCarrito
    }

    // Elimina un viaje específico del carrito
    fun eliminarDelCarrito(viaje: Viaje) {
        listaCarrito.remove(viaje)
    }

    // Elimina un viaje según su posición en la lista
    fun eliminarPorPosicion(index: Int) {
        if (index in 0 until listaCarrito.size) {
            listaCarrito.removeAt(index)
        }
    }

    fun limpiarCarrito() {
        listaCarrito.clear()
    }

    // Calcula el costo total de los viajes en el carrito
    fun obtenerTotal(): Double {
        return listaCarrito.sumOf { it.precio.toDoubleOrNull() ?: 0.0 }
    }
}