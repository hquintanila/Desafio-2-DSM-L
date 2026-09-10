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
                Toast.makeText(this, "${viaje.titulo} Agregado al Carrito", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "No Se Encontraron Viajes Registrados.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val listaViajes = mutableListOf<Viaje>()
                for (document in result) {
                    try {
                        val viaje = document.toObject(Viaje::class.java)
                        viaje.id = document.id
                        listaViajes.add(viaje)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error Procesando Item: ${e.message}")
                    }
                }

                adapter.updateLista(listaViajes)
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error al Conectar con Firestore: ${e.message}")
                Toast.makeText(this, "Error al Cargar Catálogo desde el Servidor", Toast.LENGTH_SHORT).show()
            }
    }
}