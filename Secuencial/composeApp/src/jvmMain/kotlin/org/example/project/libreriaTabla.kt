package org.example.project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
data class Usuario(val nombre: String, val password: String)

val usuariosPrueba = listOf(
    Usuario("valerie",  "1234"),
    Usuario("ana",      "abc"),
    Usuario("carlos",   "pass1"),
    Usuario("sofia",    "qwerty"),
    Usuario("sofia2",    "qwerty"),
    Usuario("sofia4",    "qwerty"),
    Usuario("sofia5",    "qwerty"),
    Usuario("sofia6",    "qwerty"),
    Usuario("sofia7",    "qwerty"),
    Usuario("sofia8",    "qwerty"),
    Usuario("tobiichi",    "A"),
    Usuario(nombre = "Himura", password = "hopa"),
)

// ─── Constantes de color para los estados de la fila ─────────────────────────
val ColorRevisando  = Color(0xFFFFF9C4)   // amarillo suave — está mirando aquí
val ColorEncontrado = Color(0xFFC8E6C9)   // verde suave    — encontrado ✅
val ColorDenegado   = Color(0xFFFFCDD2)   // rojo suave     — no encontrado ❌

@Composable
fun FilaEncabezado() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Usuario",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "Password",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun FilaUsuario(
    usuario: Usuario,
    esImpar: Boolean,
    estaRevisando: Boolean,
    estaEncontrado: Boolean,
    estaDenegado: Boolean
) {
    // El color depende del estado — en ese orden de prioridad
    val colorFondo = when {
        estaEncontrado -> ColorEncontrado
        estaDenegado   -> ColorDenegado
        estaRevisando  -> ColorRevisando
        esImpar        -> MaterialTheme.colorScheme.surfaceVariant
        else           -> MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = usuario.nombre,
            modifier = Modifier.weight(1f),
            fontWeight = if (estaRevisando || estaEncontrado) FontWeight.Bold
            else FontWeight.Normal
        )
        Text(
            text = usuario.password,
            modifier = Modifier.weight(1f),
            fontWeight = if (estaRevisando || estaEncontrado) FontWeight.Bold
            else FontWeight.Normal
        )
    }
}

@Composable
fun TablaUsuarios(
    usuarios: List<Usuario>,
    filaActiva: Int = -1,       // índice que se está revisando, -1 = ninguna
    filaEncontrada: Int = -1,   // índice del usuario encontrado, -1 = ninguno
    filaDenegada: Int = -1      // índice del último revisado si no encontró
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(250.dp)

    ) {
        Text(
            text = "Base de datos — usuarios",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Column {
                FilaEncabezado()
                HorizontalDivider()

                LazyColumn {
                    items(usuarios.indices.toList()) { index ->
                        FilaUsuario(
                            usuario        = usuarios[index],
                            esImpar        = index % 2 != 0,
                            estaRevisando  = index == filaActiva,
                            estaEncontrado = index == filaEncontrada,
                            estaDenegado   = index == filaDenegada
                        )
                        if (index < usuarios.lastIndex) {
                            HorizontalDivider(thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}