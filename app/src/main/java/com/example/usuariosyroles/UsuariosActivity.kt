package com.example.comercializadoraangel

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class UsuariosActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: UsuarioAdapter
    private lateinit var tvVacio: TextView
    private var roles: List<Rol> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuarios)

        db = AppDatabase.getDatabase(applicationContext)
        val recycler = findViewById<RecyclerView>(R.id.recyclerUsuarios)
        val btnNuevo = findViewById<Button>(R.id.btnNuevoUsuario)
        val btnVolver = findViewById<Button>(R.id.btnVolverUsuarios)
        tvVacio = findViewById(R.id.tvUsuariosVacio)

        adapter = UsuarioAdapter { usuario -> confirmarCambioEstado(usuario) }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnNuevo.setOnClickListener { mostrarFormulario() }
        btnVolver.setOnClickListener { finish() }

        lifecycleScope.launch {
            AppDatabase.inicializarRoles(db)
            db.usuarioDao().observarUsuarios().collect { lista ->
                adapter.enviarLista(lista)
                tvVacio.visibility = if (lista.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun mostrarFormulario() {
        lifecycleScope.launch {
            roles = db.rolDao().obtenerRolesActivos()
            if (roles.isEmpty()) {
                Toast.makeText(this@UsuariosActivity, "No hay roles activos disponibles.", Toast.LENGTH_LONG).show()
                return@launch
            }
            val vista = LayoutInflater.from(this@UsuariosActivity)
                .inflate(R.layout.dialog_nuevo_usuario, null, false)
            val etNombre = vista.findViewById<EditText>(R.id.etNombreCompleto)
            val etUsername = vista.findViewById<EditText>(R.id.etNuevoUsername)
            val etPassword = vista.findViewById<EditText>(R.id.etNuevaPassword)
            val spRol = vista.findViewById<Spinner>(R.id.spRolUsuario)

            spRol.adapter = ArrayAdapter(
                this@UsuariosActivity,
                android.R.layout.simple_spinner_dropdown_item,
                roles.map { it.nombre_rol }
            )

            val dialog = AlertDialog.Builder(this@UsuariosActivity)
                .setTitle("Nuevo usuario")
                .setView(vista)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Guardar", null)
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val nombre = etNombre.text.toString().trim()
                    val username = etUsername.text.toString().trim()
                    val password = etPassword.text.toString()

                    when {
                        nombre.isBlank() -> etNombre.error = "Ingresa el nombre completo"
                        username.isBlank() -> etUsername.error = "Ingresa un nombre de usuario"
                        password.length < 6 -> etPassword.error = "La contraseña debe tener al menos 6 caracteres"
                        else -> lifecycleScope.launch {
                            if (db.usuarioDao().contarUsername(username) > 0) {
                                etUsername.error = "El nombre de usuario ya está registrado"
                                return@launch
                            }
                            try {
                                db.usuarioDao().insertar(
                                    Usuario(
                                        username = username,
                                        password_hash = PasswordHasher.hash(password),
                                        nombre_completo = nombre,
                                        id_rol = roles[spRol.selectedItemPosition].id_rol,
                                        activo = true
                                    )
                                )
                                Toast.makeText(this@UsuariosActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } catch (_: Exception) {
                                etUsername.error = "No se pudo guardar. Comprueba que el username sea único."
                            }
                        }
                    }
                }
            }
            dialog.show()
        }
    }

    private fun confirmarCambioEstado(item: UsuarioConRol) {
        val accion = if (item.usuario.activo) "desactivar" else "activar"
        AlertDialog.Builder(this)
            .setTitle("Cambiar estado")
            .setMessage("¿Quieres $accion la cuenta de ${item.usuario.nombre_completo}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar") { _, _ ->
                lifecycleScope.launch {
                    db.usuarioDao().cambiarEstado(item.usuario.id_usuario, !item.usuario.activo)
                    Toast.makeText(
                        this@UsuariosActivity,
                        if (item.usuario.activo) "Usuario desactivado" else "Usuario activado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .show()
    }
}
