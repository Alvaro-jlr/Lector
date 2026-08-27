package com.ajlr.lector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ajlr.lector.data.EstadoLectura
import com.ajlr.lector.data.Obra
import com.ajlr.lector.data.TipoObra
import com.ajlr.lector.ui.theme.PurpleDark
import com.ajlr.lector.ui.theme.NebulaBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(viewModel: ObraViewModel) {
    LaunchedEffect(Unit) {
        viewModel.setTiposPermitidos(listOf(TipoObra.MANGAS, TipoObra.MANHWAS, TipoObra.MANHUAS))
    }

    val obras by viewModel.obras.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val filtro by viewModel.filtro.collectAsState()
    var mostrarDialogoCategoria by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mangas/Manhwas/Manhuas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.agregarObra(
                    Obra(
                        titulo = "Manga de prueba",
                        portadaUrl = "https://picsum.photos/200/300",
                        descripcion = "Descripción de prueba",
                        tipo = TipoObra.MANGAS,
                        idiomaOriginal = "ja"
                    )
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar obra")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(PurpleDark)) {
            NebulaBackground(modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = filtro.categoriaId == null,
                            onClick = { viewModel.setCategoriaSeleccionada(null) },
                            label = { Text("Todos") }
                        )
                    }
                    items(categorias, key = { it.id }) { categoria ->
                        FilterChip(
                            selected = filtro.categoriaId == categoria.id,
                            onClick = { viewModel.setCategoriaSeleccionada(categoria.id) },
                            label = { Text(categoria.nombre) }
                        )
                    }
                    item {
                        AssistChip(
                            onClick = { mostrarDialogoCategoria = true },
                            label = { Text("+ Nueva") }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(obras, key = { it.id }) { obra ->
                        ObraCard(obra)
                    }
                }
            }
        }
    }

    if (mostrarDialogoCategoria) {
        var nombreNuevaCategoria by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { mostrarDialogoCategoria = false },
            title = { Text("Nueva categoría") },
            text = {
                OutlinedTextField(
                    value = nombreNuevaCategoria,
                    onValueChange = { nombreNuevaCategoria = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nombreNuevaCategoria.isNotBlank()) {
                        viewModel.crearCategoria(nombreNuevaCategoria)
                    }
                    mostrarDialogoCategoria = false
                }) { Text("Crear") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCategoria = false }) { Text("Cancelar") }
            }
        )
    }
}