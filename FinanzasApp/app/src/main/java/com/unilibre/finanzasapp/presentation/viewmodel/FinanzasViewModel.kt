package com.unilibre.finanzasapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.finanzasapp.domain.model.Transaccion
import com.unilibre.finanzasapp.domain.repository.TransaccionRepository
import com.unilibre.finanzasapp.domain.usecase.AgregarTransaccionUseCase
import com.unilibre.finanzasapp.domain.usecase.ObtenerResumenUseCase
import com.unilibre.finanzasapp.domain.usecase.ResumenFinanciero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanzasViewModel @Inject constructor(
    private val repository: TransaccionRepository,
    private val agregarUseCase: AgregarTransaccionUseCase,
    private val resumenUseCase: ObtenerResumenUseCase
) : ViewModel() {

    val transacciones: StateFlow<List<Transaccion>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _resumen = MutableStateFlow(ResumenFinanciero(0.0, 0.0, 0.0))
    val resumen: StateFlow<ResumenFinanciero> = _resumen.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            transacciones.collect { _resumen.value = resumenUseCase() }
        }
    }

    fun agregar(descripcion: String, monto: String, tipo: String, categoria: String) {
        viewModelScope.launch {
            val resultado = agregarUseCase(
                Transaccion(
                    descripcion = descripcion,
                    monto = monto.toDoubleOrNull() ?: 0.0,
                    tipo = tipo,
                    categoria = categoria
                )
            )
            resultado.onFailure { _error.value = it.message }
        }
    }

    fun eliminar(t: Transaccion) = viewModelScope.launch { repository.eliminar(t) }
    fun limpiarError() { _error.value = null }
}