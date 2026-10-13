package com.example.comercializadoraangel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Transaction
    @Query("SELECT * FROM USUARIO ORDER BY nombre_completo COLLATE NOCASE ASC")
    fun observarUsuarios(): Flow<List<UsuarioConRol>>

    @Query("SELECT COUNT(*) FROM USUARIO WHERE username = :username COLLATE NOCASE")
    suspend fun contarUsername(username: String): Int

    @Query("SELECT * FROM USUARIO WHERE username = :username COLLATE NOCASE LIMIT 1")
    suspend fun buscarPorUsername(username: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Query("UPDATE USUARIO SET activo = :activo WHERE id_usuario = :id")
    suspend fun cambiarEstado(id: Long, activo: Boolean)
}
