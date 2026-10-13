package com.example.comercializadoraangel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction

@Database(entities = [Rol::class, Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rolDao(): RolDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "comercializadora_angel.db"
                ).build().also { INSTANCE = it }
            }

        suspend fun inicializarRoles(db: AppDatabase) {
            if (db.rolDao().contarRoles() == 0) {
                db.rolDao().insertarRoles(
                    listOf(
                        Rol(nombre_rol = "Administrador", descripcion = "Gestiona usuarios, roles y configuración."),
                        Rol(nombre_rol = "Operador/Bodeguero", descripcion = "Realiza tareas operativas de bodega.")
                    )
                )
            }
        }
    }
}
