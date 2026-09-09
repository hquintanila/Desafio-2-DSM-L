package com.example.desafio_2dsm_l.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_2dsm_l.databinding.ItemViajeBinding
import com.example.desafio_2dsm_l.model.Viaje

class ViajeAdapter(
    private var listaViajes: List<Viaje> = emptyList(),
    private val onItemClick: (Viaje) -> Unit
) : RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder>() {

    inner class ViajeViewHolder(val binding: ItemViajeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val binding = ItemViajeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        val viaje = listaViajes[position]
        with(holder.binding) {
            tvTitulo.text = viaje.titulo
            tvUbicacion.text = viaje.ubicacion
            tvDuracion.text = viaje.duracion
            tvPrecio.text = "$${viaje.precio}"

            // Carga de imagen remota con Glide
            Glide.with(root.context)
                .load(viaje.imagenUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivImagenViaje)

            root.setOnClickListener { onItemClick(viaje) }
        }
    }

    override fun getItemCount(): Int = listaViajes.size

    // Función para actualizar la lista cuando carguen los datos desde Firebase
    fun updateLista(nuevaLista: List<Viaje>) {
        listaViajes = nuevaLista
        notifyDataSetChanged()
    }
}