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
fun NovelScreen(viewModel: ObraViewModel) {
    LaunchedEffect(Unit) {
        viewModel.setTiposPermitidos(listOf(TipoObra.LIGHT_NOVEL, TipoObra.WEB_NOVEL))
    }

    val obras by viewModel.obras.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Web/Light Novels") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.agregarObra(
                    Obra(
                        titulo = "Novela de prueba",
                        portadaUrl = "https://picsum.photos/200/300",
                        descripcion = "Descripción de prueba",
                        tipo = TipoObra.WEB_NOVEL,
                        idiomaOriginal = "en"
                    )
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar novela")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(PurpleDark)) {
            NebulaBackground(modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = filtro.estado == null,
                            onClick = { viewModel.setFiltroEstado(null) },
                            label = { Text("Todos los estados") }
                        )
                    }
                    items(EstadoLectura.entries.toList()) { estado ->
                        FilterChip(
                            selected = filtro.estado == estado,
                            onClick = {
                                viewModel.setFiltroEstado(if (filtro.estado == estado) null else estado)
                            },
                            label = { Text(estado.name) }
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
}

@Composable
fun ObraCard(obra: Obra) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = obra.portadaUrl,
                contentDescription = obra.titulo,
                modifier = Modifier.width(60.dp).height(90.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(obra.titulo, style = MaterialTheme.typography.titleMedium)
                Text(obra.tipo.name, style = MaterialTheme.typography.bodySmall)
                Text("Capítulo ${obra.capituloActual}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}