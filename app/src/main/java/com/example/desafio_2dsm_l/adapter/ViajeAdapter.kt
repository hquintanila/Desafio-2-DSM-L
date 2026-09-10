package com.example.desafio_2dsm_l.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_2dsm_l.R
import com.example.desafio_2dsm_l.databinding.ItemViajeBinding
import com.example.desafio_2dsm_l.model.Viaje

class ViajeAdapter(
    private var listaViajes: List<Viaje> = emptyList(),
    private val onItemClick: (Viaje) -> Unit,
    private val onAddCartClick: ((Viaje) -> Unit)? = null,
    private val showAddButton: Boolean = true
) : RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder>() {

    constructor(
        onItemClick: (Viaje) -> Unit,
        onAddCartClick: (Viaje) -> Unit
    ) : this(emptyList(), onItemClick, onAddCartClick, true)

    inner class ViajeViewHolder(val binding: ItemViajeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viaje: Viaje) {
            binding.tvTitulo.text = viaje.titulo
            binding.tvUbicacion.text = viaje.ubicacion
            binding.tvDuracion.text = viaje.duracion
            binding.tvPrecio.text = "$${viaje.precio}"

            // Obtener el identificador dinámico de drawable (ej: "alaska", "cartagena", "janeiro")
            val context = itemView.context
            val resIdByName = if (!viaje.imagenUrl.isNullOrEmpty()) {
                context.resources.getIdentifier(viaje.imagenUrl.trim(), "drawable", context.packageName)
            } else 0

            // Carga de imagen con prioridades: Resource ID explícito -> Nombre Drawable -> URL Web -> Fallback
            when {
                viaje.imagenResId != 0 -> {
                    binding.ivImagenViaje.setImageResource(viaje.imagenResId)
                }
                resIdByName != 0 -> {
                    Glide.with(context)
                        .load(resIdByName)
                        .placeholder(R.drawable.cancun1)
                        .error(R.drawable.cancun1)
                        .into(binding.ivImagenViaje)
                }
                !viaje.imagenUrl.isNullOrEmpty() -> {
                    Glide.with(context)
                        .load(viaje.imagenUrl)
                        .placeholder(R.drawable.cancun1)
                        .error(R.drawable.cancun1)
                        .into(binding.ivImagenViaje)
                }
                else -> {
                    binding.ivImagenViaje.setImageResource(R.drawable.cancun1)
                }
            }

            // Visibilidad y comportamiento del botón Agregar al Carrito
            if (showAddButton && onAddCartClick != null) {
                binding.btnAgregar.visibility = View.VISIBLE
                binding.btnAgregar.setOnClickListener {
                    onAddCartClick.invoke(viaje)
                }
            } else {
                binding.btnAgregar.visibility = View.GONE
            }

            // Clic en la tarjeta para abrir vista de detalle
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