package com.unilibre.clima.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "historial_ciudades")
data class CiudadEntity(
    @PrimaryKey val nombre: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface HistorialDao {
    @Query("SELECT * FROM historial_ciudades ORDER BY timestamp DESC LIMIT 5")
    fun getUltimasCinco(): Flow<List<CiudadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ciudad: CiudadEntity)
}
