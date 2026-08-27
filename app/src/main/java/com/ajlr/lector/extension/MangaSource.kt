package com.ajlr.lector.extension

data class SManga(
    val url: String,
    val title: String,
    val artist: String = "",
    val author: String = "",
    val description: String = "",
    val genre: List<String> = emptyList(),
    val status: Int = 0,
    val thumbnailUrl: String = ""
)

data class SChapter(
    val url: String,
    val name: String,
    val dateUpload: Long = 0L,
    val chapterNumber: Float = 0f
)

interface MangaSource {
    val id: Long
    val name: String
    val baseUrl: String
    val lang: String

    suspend fun buscarMangas(query: String): List<SManga>
    suspend fun obtenerDetalles(mangaUrl: String): SManga
    suspend fun obtenerCapitulos(mangaUrl: String): List<SChapter>
    suspend fun obtenerPaginas(chapterUrl: String): List<String>
}