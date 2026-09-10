package com.example.evaluacion1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onItemClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvItemAvatar)
        val tvNombreFormateado: TextView = itemView.findViewById(R.id.tvItemNombreFormateado)
        val tvEmail: TextView = itemView.findViewById(R.id.tvItemEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        
        // Requisito de pauta: formato "ID - Nombres Apellidos"
        holder.tvNombreFormateado.text = "${usuario.id} - ${usuario.nombres} ${usuario.apellidos}"
        holder.tvEmail.text = usuario.email

        // Iniciales para el avatar
        val inicialNombre = usuario.nombres.firstOrNull()?.uppercaseChar() ?: 'U'
        val inicialApellido = usuario.apellidos.firstOrNull()?.uppercaseChar() ?: 'S'
        holder.tvAvatar.text = "$inicialNombre$inicialApellido"

        holder.itemView.setOnClickListener {
            onItemClick(usuario)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }
}
