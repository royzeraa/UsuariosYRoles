package com.example.comercializadoraangel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "USUARIO",
    foreignKeys = [
        ForeignKey(
            entity = Rol::class,
            parentColumns = ["id_rol"],
            childColumns = ["id_rol"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["username"], unique = true), Index("id_rol")]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id_usuario: Long = 0,
    val username: String,
    val password_hash: String,
    val nombre_completo: String,
    val id_rol: Long,
    val activo: Boolean = true
)
