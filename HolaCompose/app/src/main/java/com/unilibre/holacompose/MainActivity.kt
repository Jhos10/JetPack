package com.unilibre.holacompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ahora mostramos la Tarjeta de Perfil en la actividad principal
                    TarjetaPerfil(
                        nombre = "Jhostin Posada Cano",
                        cargo = "Estudiante Ing. Sistemas | Desarrollador",
                        descripcion = "Apasionado por el desarrollo en Kotlin, Python y la creación de herramientas de Inteligencia Artificial."
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaBienvenida(nombre: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "¡Hola, $nombre!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenido a Jetpack Compose",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TarjetaPerfil(nombre: String, cargo: String, descripcion: String, modifier: Modifier = Modifier) {
    // Usamos Card en lugar de Surface para tener sombras (elevation) por defecto
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0B192C) // Azul muy oscuro
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // Centra todo horizontalmente
        ) {
            // Imagen de Avatar Circular
            Image(
                // IMPORTANTE: Cambia "android.R.drawable.ic_menu_camera" por el nombre de una imagen
                // que hayas arrastrado a tu carpeta res/drawable (ej: R.drawable.mi_foto)
                painter = painterResource(id = android.R.drawable.ic_menu_camera), 
                contentDescription = "Foto de perfil de $nombre",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Aquí ocurre la magia del recorte circular
                    .background(Color.LightGray)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Textos del perfil
            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = cargo,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF64B5F6) // Azul claro para contrastar
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Íconos de redes/contacto usando Row para alinearlos horizontalmente
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color.White)
                Icon(imageVector = Icons.Default.Person, contentDescription = "GitHub/Portfolio", tint = Color.White)
                Icon(imageVector = Icons.Default.Share, contentDescription = "Compartir", tint = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTarjeta() {
    MaterialTheme {
        TarjetaBienvenida(nombre = "Estudiante")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTarjetaPerfil() {
    MaterialTheme {
        TarjetaPerfil(
            nombre = "Jhostin Posada Cano",
            cargo = "Estudiante Ing. Sistemas | Desarrollador",
            descripcion = "Apasionado por el desarrollo en Kotlin, Python y la creación de herramientas de Inteligencia Artificial."
        )
    }
}
