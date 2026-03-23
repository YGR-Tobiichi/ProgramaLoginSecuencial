package org.example.project

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.nio.file.Files.size

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Login",
        state = rememberWindowState(size = DpSize(480.dp, 600.dp))
    ){
        LoginUsuario()
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "BD",
        state = rememberWindowState(size = DpSize(480.dp, 600.dp))
    ) {
        Pantalla2BaseDeDatos()
    }
}