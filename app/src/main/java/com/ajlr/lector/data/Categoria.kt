package com.ajlr.lector.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val orden: Int = 0
)

@Entity(tableName = "obra_categoria", primaryKeys = ["obraId", "categoriaId"])
data class ObraCategoriaCrossRef(
    val obraId: Long,
    val categoriaId: Long
)