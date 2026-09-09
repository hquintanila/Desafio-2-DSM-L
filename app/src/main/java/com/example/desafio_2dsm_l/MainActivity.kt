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
                val listaViajes = mutableListOf<Viaje>()
                for (document in result) {
                    val viaje = document.toObject(Viaje::class.java)
                    viaje.id = document.id
                    listaViajes.add(viaje)
                }
                adapter.updateLista(listaViajes)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error Al Cargar Datos: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}