package com.example.comercializadoraangel

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ROL",
    indices = [Index(value = ["nombre_rol"], unique = true)]
)
data class Rol(
    @PrimaryKey(autoGenerate = true) val id_rol: Long = 0,
    val nombre_rol: String,
    val descripcion: String = "",
    val activo: Boolean = true
)
