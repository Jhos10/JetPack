package com.unilibre.asistenterecetas.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReceta(receta: RecetaEntity)
    
    @Query("SELECT * FROM recetas_favoritas")
    fun getFavoritas(): Flow<List<RecetaEntity>>

    @Delete
    suspend fun eliminarReceta(receta: RecetaEntity)
}

@Database(entities = [RecetaEntity::class], version = 1, exportSchema = false)
abstract class RecetasDatabase : RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
}
