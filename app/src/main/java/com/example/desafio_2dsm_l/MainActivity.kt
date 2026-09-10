package com.example.desafio_2dsm_l

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio_2dsm_l.adapter.ViajeAdapter
import com.example.desafio_2dsm_l.databinding.ActivityMainBinding
import com.example.desafio_2dsm_l.model.Viaje
import com.google.firebase.auth.FirebaseAuth
import com.example.desafio_2dsm_l.R
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

        // Configurar la Toolbar como barra de acciones principal
        setSupportActionBar(binding.toolbarMain)

        setupRecyclerView()
        cargarViajesDesdeFirestore()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                cerrarSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cerrarSesion() {
        auth.signOut()
        Toast.makeText(this, "Sesión Cerrada Correctamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun setupRecyclerView() {
        adapter = ViajeAdapter { viaje ->
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
        }
        binding.rvViajes.layoutManager = LinearLayoutManager(this)
        binding.rvViajes.adapter = adapter
    }

    private fun cargarViajesDesdeFirestore() {
        db.collection("viajes")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // Validación: Si Firestore no tiene registros, cargamos los 3 destinos locales
                    cargarDatosLocales("Mostrando Catálogo Local de Viajes.")
                    return@addOnSuccessListener
                }

                val listaViajes = mutableListOf<Viaje>()
                for ((index, document) in result.withIndex()) {
                    val viaje = document.toObject(Viaje::class.java)
                    viaje.id = document.id

                    // Validación: Si la URL de la imagen en Firestore es vacía o nula, asignamos la local por título o posición
                    if (viaje.imagenUrl.isNullOrBlank() && viaje.imagenResId == 0) {
                        viaje.imagenResId = obtenerImagenLocalPorDefecto(viaje.titulo, index)
                    }

                    listaViajes.add(viaje)
                }
                adapter.updateLista(listaViajes)
            }
            .addOnFailureListener { exception ->
                // Validación: En caso de error de red/Firestore, se activa la lista de respaldo
                cargarDatosLocales("Aviso: Conectando en Modo Offline. Mostrando Catálogo Local.")
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
            tituloLower.contains("arqueologia") || tituloLower.contains("arqueológica") || tituloLower.contains("chichen") -> R.drawable.arqueologia
            else -> {
                val imagenes = listOf(R.drawable.cancun1, R.drawable.cenotes3, R.drawable.arqueologia)
                imagenes[indice % imagenes.size]
            }
        }
    }

    private fun obtenerListaLocales(): List<Viaje> {
        return listOf(
            Viaje(
                id = "local_1",
                titulo = "Cancún Todo Incluido",
                descripcion = "Disfruta de Playas Caribeñas de Arena Blanca y Aguas Turquesas.",
                precio = 499.00,
                duracion = "5 Días / 4 Noches",
                ubicacion = "Quintana Roo, México",
                imagenResId = R.drawable.cancun1
            ),
            Viaje(
                id = "local_2",
                titulo = "Exploración de Cenotes",
                descripcion = "Sumérgete en los Mágicos Pozos Naturales Sagrados de la Península.",
                precio = 299.00,
                duracion = "3 Días / 2 Noches",
                ubicacion = "Yucatán, México",
                imagenResId = R.drawable.cenotes3
            ),
            Viaje(
                id = "local_3",
                titulo = "Ruta Arqueológica",
                descripcion = "Descubre las Majestuosas Ruinas Mayas y su Historia Ancestral.",
                precio = 399.00,
                duracion = "4 Días / 3 Noches",
                ubicacion = "Chichén Itzá, México",
                imagenResId = R.drawable.arqueologia
            )
        )
    }
}