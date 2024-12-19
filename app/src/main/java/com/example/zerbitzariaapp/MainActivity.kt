package com.example.zerbitzariaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    // Colores de la paleta
    val backgroundColor = Color(0xFFBFAB92)
    val textColor = Color(0xFF1C1107)
    val buttonColor = Color(0xFF69472C)
    val hintColor = Color(0xFFF8F3E9)

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Diseño principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_michisuji), // Reemplaza con tu recurso
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 40.dp)
            )

            // Título
            Text(
                text = "Saioa hasi",
                color = textColor,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Campo de Usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Izena") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF8F3E9),
                    unfocusedBorderColor = Color(0xFFBFAB92),
                    unfocusedLabelColor = Color(0xFF755A3F)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Pasahitza") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF8F3E9),
                    focusedBorderColor = Color(0xFF69472C),
                    unfocusedBorderColor = Color(0xFFBFAB92),
                    cursorColor = Color(0xFF69472C),
                    focusedLabelColor = Color(0xFF69472C),
                    unfocusedLabelColor = Color(0xFF755A3F)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Botón de Inicio de Sesión
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        navController.navigate("main_screen")
                    } else {
                        // Mostrar mensaje de error si los campos están vacíos
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(horizontal = 100.dp)
                    .height(50.dp)
            ) {
                Text("Saioa hasi", color = hintColor, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController) }
        composable("main_screen") { MainScreen(navController, username = "Izena") }
        composable("mesa_screen") { MesaScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, username: String) {
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_michisuji),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = username,
                    color = Color(0xFF1C1107),
                    fontSize = 20.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                listOf("Produktuak", "Komandak", "Eskaerak", "Txata").forEach { label ->
                    Button(
                        onClick = {
                            if (label == "Komandak") {
                                navController.navigate("mesa_screen")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(label, color = textColor, fontSize = 18.sp)
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("login_screen") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
                .size(width = 150.dp, height = 50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Saioa itxi", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun MesaScreen(navController: NavHostController) {
    // Colores
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Encabezado: logo y nombre de usuario
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo en la esquina superior izquierda
            Image(
                painter = painterResource(id = R.drawable.logo_michisuji),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp) // Tamaño del logo ajustado
            )

            // Nombre del usuario en la esquina superior derecha
            Text(
                text = "Izena", // Cambia esto para que refleje el nombre del usuario registrado
                color = Color(0xFF1C1107),
                fontSize = 20.sp
            )
        }

        // Contenido principal: título y botones centrados
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Título
            Text(
                text = "Mahaia aukeratu",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botones para las mesas
            for (row in 0..3) { // 4 filas (2 botones por fila)
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    for (col in 1..2) { // 2 botones por fila
                        val tableNumber = row * 2 + col
                        Button(
                            onClick = { /* Acción del botón de mesa */ },
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            modifier = Modifier
                                .size(100.dp), // Tamaño cuadrado para los botones
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("$tableNumber", color = textColor, fontSize = 20.sp)
                        }
                    }
                }
            }
        }

        // Botón "Atzera" abajo a la izquierda
        Button(
            onClick = { navController.popBackStack() }, // Volver a la pantalla anterior
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .align(Alignment.BottomStart) // Alineación abajo a la izquierda
                .padding(8.dp)
                .size(width = 150.dp, height = 50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Atzera", color = Color.White, fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController, username = "Izena")
}

@Preview(showBackground = true)
@Composable
fun MesaScreenPreview() {
    val navController = rememberNavController()
    MesaScreen(navController = navController)
}
