package com.example.desafio_2dsm_l.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_2dsm_l.R
import com.example.desafio_2dsm_l.databinding.ItemViajeBinding
import com.example.desafio_2dsm_l.model.Viaje

class ViajeAdapter(
    private var listaViajes: List<Viaje> = emptyList(),
    private val onItemClick: (Viaje) -> Unit
) : RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder>() {

    // Constructor secundario para inicializar como: ViajeAdapter { viaje -> ... }
    constructor(onItemClick: (Viaje) -> Unit) : this(emptyList(), onItemClick)

    inner class ViajeViewHolder(val binding: ItemViajeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viaje: Viaje) {
            binding.tvTitulo.text = viaje.titulo
            binding.tvUbicacion.text = viaje.ubicacion
            binding.tvDuracion.text = viaje.duracion
            binding.tvPrecio.text = "$${viaje.precio}"

            if (viaje.imagenResId != 0) {
                binding.ivImagenViaje.setImageResource(viaje.imagenResId)
            } else if (viaje.imagenUrl.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(viaje.imagenUrl)
                    .placeholder(R.drawable.cancun1)
                    .error(R.drawable.cancun1)
                    .into(binding.ivImagenViaje)
            } else {
                binding.ivImagenViaje.setImageResource(R.drawable.cancun1)
            }

            binding.cardViaje.setOnClickListener {
                onItemClick(viaje)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViajeViewHolder {
        val binding = ItemViajeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViajeViewHolder, position: Int) {
        holder.bind(listaViajes[position])
    }

    override fun getItemCount(): Int = listaViajes.size

    fun updateLista(nuevaLista: List<Viaje>) {
        this.listaViajes = nuevaLista
        notifyDataSetChanged()
    }
}