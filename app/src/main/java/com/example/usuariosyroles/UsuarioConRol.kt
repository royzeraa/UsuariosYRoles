package com.example.comercializadoraangel

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioConRol(
    @Embedded val usuario: Usuario,
    @Relation(parentColumn = "id_rol", entityColumn = "id_rol")
    val rol: Rol
)
