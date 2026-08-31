package com.ajlr.lector.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ajlr.lector.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LibraryFiltro(
    val tiposPermitidos: List<TipoObra>? = null,
    val idioma: String? = null,
    val estado: EstadoLectura? = null,
    val categoriaId: Long? = null
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ObraViewModel(application: Application) : AndroidViewModel(application) {

    private val obraDao = AppDatabase.obtenerInstancia(application).obraDao()
    private val categoriaDao = AppDatabase.obtenerInstancia(application).categoriaDao()

    private val _filtro = MutableStateFlow(LibraryFiltro())
    val filtro: StateFlow<LibraryFiltro> = _filtro

    val categorias: StateFlow<List<Categoria>> = categoriaDao.obtenerTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val obras: StateFlow<List<Obra>> = _filtro
        .flatMapLatest { f ->
            val baseFlow = if (f.categoriaId != null) {
                categoriaDao.obtenerObrasDeCategoria(f.categoriaId)
            } else {
                obraDao.obtenerTodas()
            }

            baseFlow.map { lista ->
                lista.filter { obra ->
                    val cumpleTipo = f.tiposPermitidos == null || obra.tipo in f.tiposPermitidos
                    val cumpleIdioma = f.idioma == null || obra.idiomaOriginal == f.idioma
                    val cumpleEstado = f.estado == null || obra.estado == f.estado

                    cumpleTipo && cumpleIdioma && cumpleEstado
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTiposPermitidos(tipos: List<TipoObra>) {
        if (_filtro.value.tiposPermitidos != tipos) {
            _filtro.value = _filtro.value.copy(tiposPermitidos = tipos)
        }
    }

    fun setFiltroEstado(estado: EstadoLectura?) {
        _filtro.value = _filtro.value.copy(estado = estado)
    }

    fun setCategoriaSeleccionada(categoriaId: Long?) {
        _filtro.value = _filtro.value.copy(categoriaId = categoriaId)
    }

    fun agregarObra(obra: Obra) {
        viewModelScope.launch { obraDao.insertar(obra) }
    }

    fun actualizarCapitulo(obraId: Long, capitulo: Float) {
        viewModelScope.launch { obraDao.actualizarCapitulo(obraId, capitulo) }
    }

    fun categoriasFiltradas(esNovela: Boolean): Flow<List<Categoria>> =
        categoriaDao.obtenerPorTipo(esNovela)

    fun crearCategoria(nombre: String, esParaNovelas: Boolean) {
        viewModelScope.launch {
            categoriaDao.insertar(Categoria(nombre = nombre, orden = categorias.value.size, esParaNovelas = esParaNovelas))
        }
    }

    fun obtenerCategoriaIdsDeObra(obraId: Long): Flow<List<Long>> =
        categoriaDao.obtenerCategoriaIdsDeObra(obraId)

    fun asignarACategoria(obraId: Long, categoriaId: Long) {
        viewModelScope.launch {
            categoriaDao.asignarObraACategoria(ObraCategoriaCrossRef(obraId, categoriaId))
        }
    }

    fun quitarDeCategoria(obraId: Long, categoriaId: Long) {
        viewModelScope.launch {
            categoriaDao.quitarObraDeCategoria(obraId, categoriaId)
        }
    }

    fun actualizarEstado(obraId: Long, estado: EstadoLectura) {
        viewModelScope.launch { obraDao.actualizarEstado(obraId, estado) }
    }
}