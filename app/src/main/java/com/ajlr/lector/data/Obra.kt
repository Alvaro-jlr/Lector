package com.ajlr.lector.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

enum class TipoObra {
    MANGAS, MANHWAS, MANHUAS, LIGHT_NOVEL, WEB_NOVEL
}

enum class EstadoLectura {
    Leyendo, Pendiente
}

@Entity(tableName = "obras")
@TypeConverters(Converters::class)
data class Obra(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val titulosAlternativos: List<String> = emptyList(),
    val portadaUrl: String,
    val descripcion: String,
    val tipo: TipoObra,
    val idiomaOriginal: String,
    val traducirAEspanol: Boolean = false,
    val estado: EstadoLectura = EstadoLectura.Pendiente,
    val capituloActual: Float = 0f,
    val fuenteId: Long? = null,
    val anilistId: Int? = null,
    val novelUpdatesId: String? = null
)