package com.unilibre.asistenterecetas.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unilibre.asistenterecetas.domain.model.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaReceta(receta: Receta, onGuardar: (Receta) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = receta.nombre, style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onGuardar(receta) }) {
                    Icon(Icons.Default.Favorite, "Guardar", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(onClick = {}, label = { Text("${receta.tiempo_minutos} min") })
                SuggestionChip(onClick = {}, label = { Text(receta.dificultad) })
                SuggestionChip(onClick = {}, label = { Text("${receta.calorias} kcal") })
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Instrucciones:", style = MaterialTheme.typography.titleMedium)
            receta.pasos.forEachIndexed { index, paso ->
                Text(text = "${index + 1}. $paso", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
