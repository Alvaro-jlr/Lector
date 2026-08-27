package com.ajlr.lector.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ObraDao {

    @Insert
    suspend fun insertar(obra: Obra): Long

    @Update
    suspend fun actualizar(obra: Obra)

    @Delete
    suspend fun eliminar(obra: Obra)

    @Query("SELECT * FROM obras ORDER BY titulo ASC")
    fun obtenerTodas(): Flow<List<Obra>>

    @Query("SELECT * FROM obras WHERE id = :obraId")
    suspend fun obtenerPorId(obraId: Long): Obra?

    @Query("UPDATE obras SET capituloActual = :capitulo WHERE id = :obraId")
    suspend fun actualizarCapitulo(obraId: Long, capitulo: Float)

    @Query("""
        SELECT * FROM obras 
        WHERE (:tipo IS NULL OR tipo = :tipo)
        AND (:idioma IS NULL OR idiomaOriginal = :idioma)
        AND (:estado IS NULL OR estado = :estado)
        ORDER BY titulo ASC
    """)
    fun filtrar(
        tipo: TipoObra? = null,
        idioma: String? = null,
        estado: EstadoLectura? = null
    ): Flow<List<Obra>>
}