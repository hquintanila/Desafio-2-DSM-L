package com.example.desafio_2dsm_l

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio_2dsm_l.adapter.ViajeAdapter
import com.example.desafio_2dsm_l.databinding.ActivityMainBinding
import com.example.desafio_2dsm_l.model.CartManager
import com.example.desafio_2dsm_l.model.Viaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViajeAdapter
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)

        setupRecyclerView()
        cargarViajesDesdeFirestore()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Muestra la información del usuario encriptada en los elementos del menú
        val user = auth.currentUser
        val email = user?.email ?: "usuario@ejemplo.com"
        val emailEncriptado = encriptarEmail(email)

        menu?.findItem(R.id.action_profile_email)?.title = "Correo: $emailEncriptado"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                cerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun encriptarEmail(email: String): String {
        val partes = email.split("@")
        if (partes.size < 2) return email
        val usuario = partes[0]
        val dominio = partes[1]

        val usuarioEncriptado = if (usuario.length > 2) {
            usuario.take(2) + "*".repeat(usuario.length - 2)
        } else {
            "**"
        }
        return "$usuarioEncriptado@$dominio"
    }

    private fun cerrarSesion() {
        auth.signOut()
        CartManager.limpiarCarrito()
        Toast.makeText(this, "Sesión Cerrada Correctamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun setupRecyclerView() {
        adapter = ViajeAdapter(
            onItemClick = { viaje ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("EXTRA_TITULO", viaje.titulo)
                    putExtra("EXTRA_DESCRIPCION", viaje.descripcion)
                    putExtra("EXTRA_UBICACION", viaje.ubicacion)
                    putExtra("EXTRA_DURACION", viaje.duracion)
                    putExtra("EXTRA_PRECIO", viaje.precio)
                    putExtra("EXTRA_IMAGEN_URL", viaje.imagenUrl)
                    putExtra("EXTRA_IMAGEN_RES_ID", viaje.imagenResId)
                }
                startActivity(intent)
            },
            onAddCartClick = { viaje ->
                CartManager.agregarAlCarrito(viaje)
                Toast.makeText(this, "${viaje.titulo} agregado al Carrito", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvViajes.layoutManager = LinearLayoutManager(this)
        binding.rvViajes.adapter = adapter
    }

    private fun cargarViajesDesdeFirestore() {
        db.collection("viajes")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    cargarDatosLocales("Mostrando Catálogo Completo de Viajes.")
                    return@addOnSuccessListener
                }

                val listaViajes = mutableListOf<Viaje>()
                for ((index, document) in result.withIndex()) {
                    try {
                        val viaje = document.toObject(Viaje::class.java)
                        viaje.id = document.id

                        val tituloSeguro = viaje.titulo ?: ""
                        if (viaje.imagenUrl.isNullOrBlank() && viaje.imagenResId == 0) {
                            viaje.imagenResId = obtenerImagenLocalPorDefecto(tituloSeguro, index)
                        }

                        listaViajes.add(viaje)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error procesando item: ${e.message}")
                    }
                }

                if (listaViajes.isEmpty()) {
                    cargarDatosLocales("Mostrando Catálogo Local.")
                } else {
                    adapter.updateLista(listaViajes)
                }
            }
            .addOnFailureListener {
                cargarDatosLocales("Modo Offline. Cargando Catálogo Local.")
            }
    }

    private fun cargarDatosLocales(mensajeToast: String) {
        Toast.makeText(this, mensajeToast, Toast.LENGTH_SHORT).show()
        val listaLocales = obtenerListaLocales()
        adapter.updateLista(listaLocales)
    }

    private fun obtenerImagenLocalPorDefecto(titulo: String, indice: Int): Int {
        val tituloLower = titulo.lowercase()
        return when {
            tituloLower.contains("cancun") || tituloLower.contains("cancún") -> R.drawable.cancun1
            tituloLower.contains("cenote") -> R.drawable.cenotes3
            tituloLower.contains("colombia") || tituloLower.contains("caribe") -> R.drawable.cancun1
            tituloLower.contains("brasil") || tituloLower.contains("rio") -> R.drawable.cenotes3
            tituloLower.contains("alaska") || tituloLower.contains("glaciar") -> R.drawable.arqueologia
            else -> R.drawable.arqueologia
        }
    }

    private fun obtenerListaLocales(): List<Viaje> {
        return listOf(
            Viaje("local_1", "Cancún Todo Incluido", "Disfruta de Playas Caribeñas de Arena Blanca y Aguas Turquesas.", 499.00, "5 Días / 4 Noches", "Quintana Roo, México", imagenResId = R.drawable.cancun1),
            Viaje("local_2", "Exploración de Cenotes", "Sumérgete en los Mágicos Pozos Naturales Sagrados de la Península.", 299.00, "3 Días / 2 Noches", "Yucatán, México", imagenResId = R.drawable.cenotes3),
            Viaje("local_3", "Ruta Arqueológica", "Descubre las Majestuosas Ruinas Mayas y su Historia Ancestral.", 399.00, "4 Días / 3 Noches", "Chichén Itzá, México", imagenResId = R.drawable.arqueologia),
            Viaje("local_4", "Maravillas de Colombia", "Conoce la Hermosa Cartagena de Indias y sus playas históricas.", 550.00, "6 Días / 5 Noches", "Cartagena, Colombia", imagenResId = R.drawable.cancun1),
            Viaje("local_5", "Río de Janeiro Mágico", "Vive la Emoción del Cristo Redentor y las Playas de Copacabana.", 680.00, "7 Días / 6 Noches", "Río de Janeiro, Brasil", imagenResId = R.drawable.cenotes3),
            Viaje("local_6", "Aventura Glaciar en Alaska", "Explora Impresionantes Paisajes Helados, Auroras y Fiordos.", 890.00, "5 Días / 4 Noches", "Anchorage, Alaska", imagenResId = R.drawable.arqueologia)
        )
    }
}