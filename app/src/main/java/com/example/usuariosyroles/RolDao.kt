package com.example.comercializadoraangel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RolDao {
    @Query("SELECT * FROM ROL WHERE activo = 1 ORDER BY nombre_rol ASC")
    fun observarRolesActivos(): Flow<List<Rol>>

    @Query("SELECT * FROM ROL WHERE activo = 1 ORDER BY nombre_rol ASC")
    suspend fun obtenerRolesActivos(): List<Rol>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarRoles(roles: List<Rol>)

    @Query("SELECT COUNT(*) FROM ROL")
    suspend fun contarRoles(): Int
}
