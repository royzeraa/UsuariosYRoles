package com.example.comercializadoraangel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(
    private val onCambiarEstado: (UsuarioConRol) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    private var usuarios: List<UsuarioConRol> = emptyList()

    fun enviarLista(nuevaLista: List<UsuarioConRol>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.vincular(usuarios[position], onCambiarEstado)
    }

    override fun getItemCount(): Int = usuarios.size

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        private val username: TextView = itemView.findViewById(R.id.tvUsername)
        private val rol: TextView = itemView.findViewById(R.id.tvRol)
        private val estado: TextView = itemView.findViewById(R.id.tvEstado)
        private val botonEstado: Button = itemView.findViewById(R.id.btnCambiarEstado)

        fun vincular(item: UsuarioConRol, onCambiarEstado: (UsuarioConRol) -> Unit) {
            nombre.text = item.usuario.nombre_completo
            username.text = "@${item.usuario.username}"
            rol.text = "Rol: ${item.rol.nombre_rol}"
            estado.text = if (item.usuario.activo) "Estado: Activo" else "Estado: Inactivo"
            botonEstado.text = if (item.usuario.activo) "Desactivar" else "Activar"
            botonEstado.setOnClickListener { onCambiarEstado(item) }
        }
    }
}
