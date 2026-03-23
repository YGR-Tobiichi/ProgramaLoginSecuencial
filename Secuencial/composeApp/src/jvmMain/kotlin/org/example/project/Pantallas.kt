package org.example.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.get

var empezarBusqueda by mutableStateOf(false)

class PeticionUsuario{
    var usuario = ""
    var password = ""
    var respuesta = false
    var autorizado = false
}
var peticion = PeticionUsuario()


@Composable
fun LoginUsuario(){
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var esperando by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "PANTALLA 1.",
            modifier = Modifier.padding(8.dp, top = 100.dp), fontSize = 10.sp)
        Text(text = "LOGIN",
            modifier = Modifier.padding(8.dp, top = 0.dp), fontSize = 60.sp)

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Ingrese el usuario") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Ingrese la contraseña") }
        )

        Button(
            modifier = Modifier.padding(top = 30.dp).width(280.dp),
            shape = RoundedCornerShape(0),
            enabled = !esperando,
            onClick = {
                if (usuario.isBlank() || password.isBlank()) {
                    mensaje = "Completa todos los campos"
                    return@Button
                }

                peticion.usuario = usuario
                peticion.password = password
                mensaje = "Contactando al servidor..."
                empezarBusqueda = true
            }
        ) {
            Text(if (esperando) "Esperando..." else "Iniciar sesión")
        }

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 14.sp
            )
        }

        if(peticion.respuesta){
            peticion.respuesta = false
            mensaje = if (peticion.autorizado) "✅ Acceso concedido"
            else "❌ Credenciales incorrectas"
            esperando = false
        }
    }
}


@Composable
fun Pantalla2BaseDeDatos(){

    // Estado
    var filaActiva    by remember { mutableStateOf(-1) }
    var filaEncontrada by remember { mutableStateOf(-1) }
    var filaDenegada  by remember { mutableStateOf(-1) }

    // Mensajes
    var log by remember { mutableStateOf("Esperando peticiones...\n") }

    if(empezarBusqueda){
        // Algoritmo de busqueda
        empezarBusqueda = false
        var encontrado = false
        for (index in usuariosPrueba.indices) {

            filaActiva = index
            log += "[REVISANDO] fila $index — ${usuariosPrueba[index].nombre}\n"
            log += "[RECIBIDO] usuario: '${peticion.usuario}'\n"


            // Compara credenciales
            if (usuariosPrueba[index].nombre    == peticion.usuario &&
                usuariosPrueba[index].password  == peticion.password) {

                filaEncontrada = index
                log += "[ENCONTRADO] ✅ — rol: ${usuariosPrueba[index].nombre}\n"
                encontrado = true
                peticion.autorizado = true

                break
            }
        }

        if (!encontrado) {
            filaDenegada = filaActiva
            filaActiva   = -1
            log += "[RESULTADO] ❌ usuario no encontrado\n"
            peticion.autorizado  = false
        }

        peticion.respuesta = true
        log += "[RESPONDIDO] enviado al cliente → $encontrado\n"
    }


    // UI
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {
        Text(
            text = "PANTALLA 2.",
            modifier = Modifier.padding(8.dp, top = 20.dp),
            fontSize = 10.sp
        )
        Text(
            text = "BASE DE DATOS",
            modifier = Modifier.padding(8.dp, top = 0.dp),
            fontSize = 40.sp
        )

        // Tabla con los estados visuales
        TablaUsuarios(
            usuarios       = usuariosPrueba,

        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // Log de mensajes
        Text(
            text = "Log del servidor:",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = log,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}