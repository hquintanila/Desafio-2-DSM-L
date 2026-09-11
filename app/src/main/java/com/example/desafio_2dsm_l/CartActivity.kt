package com.example.desafio_2dsm_l

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio_2dsm_l.adapter.ViajeAdapter
import com.example.desafio_2dsm_l.databinding.ActivityCartBinding
import com.example.desafio_2dsm_l.model.CartManager

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ViajeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarCart.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        actualizarTotal()

        binding.btnClearCart.setOnClickListener {
            CartManager.limpiarCarrito()
            adapter.updateLista(emptyList())
            actualizarTotal()
            Toast.makeText(this, "Carrito Vaciado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = ViajeAdapter(
            listaViajes = CartManager.obtenerCarrito(),
            onItemClick = {},
            onAddCartClick = null,
            showAddButton = false,
            isCartMode = true,
            onItemRemoved = {
                actualizarTotal()
            }
        )
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = adapter
    }

    private fun actualizarTotal() {
        val total = CartManager.obtenerTotal()
        binding.tvTotal.text = String.format("$%.2f", total)
    }
}