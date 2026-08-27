package com.ajlr.lector.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert
    suspend fun insertar(categoria: Categoria): Long

    @Delete
    suspend fun eliminar(categoria: Categoria)

    @Query("SELECT * FROM categorias ORDER BY orden ASC")
    fun obtenerTodas(): Flow<List<Categoria>>

    @Insert
    suspend fun asignarObraACategoria(crossRef: ObraCategoriaCrossRef)

    @Query("DELETE FROM obra_categoria WHERE obraId = :obraId AND categoriaId = :categoriaId")
    suspend fun quitarObraDeCategoria(obraId: Long, categoriaId: Long)

    @Query("""
        SELECT obras.* FROM obras
        INNER JOIN obra_categoria ON obras.id = obra_categoria.obraId
        WHERE obra_categoria.categoriaId = :categoriaId
        ORDER BY obras.titulo ASC
    """)
    fun obtenerObrasDeCategoria(categoriaId: Long): Flow<List<Obra>>
}