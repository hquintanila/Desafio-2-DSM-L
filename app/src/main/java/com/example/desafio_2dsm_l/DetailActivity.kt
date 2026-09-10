package com.example.desafio_2dsm_l

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.desafio_2dsm_l.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }

        // Obtener datos enviados desde el intent
        val titulo = intent.getStringExtra("EXTRA_TITULO") ?: ""
        val descripcion = intent.getStringExtra("EXTRA_DESCRIPCION") ?: ""
        val ubicacion = intent.getStringExtra("EXTRA_UBICACION") ?: ""
        val duracion = intent.getStringExtra("EXTRA_DURACION") ?: ""
        val precio = intent.getDoubleExtra("EXTRA_PRECIO", 0.0)
        val imagenUrl = intent.getStringExtra("EXTRA_IMAGEN_URL") ?: ""
        val imagenResId = intent.getIntExtra("EXTRA_IMAGEN_RES_ID", 0)

        // Asignar los valores a la interfaz
        binding.tvDetailTitulo.text = titulo
        binding.tvDetailDescripcion.text = descripcion
        binding.tvDetailUbicacion.text = ubicacion
        binding.tvDetailDuracion.text = duracion
        binding.tvDetailPrecio.text = "$$precio"

        // Carga de imagen con prioridad: Local -> URL -> Imagen de respaldo
        if (imagenResId != 0) {
            binding.ivDetailImagen.setImageResource(imagenResId)
        } else if (imagenUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.cancun1)
                .error(R.drawable.cancun1)
                .into(binding.ivDetailImagen)
        } else {
            binding.ivDetailImagen.setImageResource(R.drawable.cancun1)
        }

        binding.btnReservar.setOnClickListener {
            Toast.makeText(this, "¡Reserva Realizada Para $titulo!", Toast.LENGTH_LONG).show()
        }
    }
}