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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
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
                    containerColor = Color(0xFFF8F3E9), // Color del fondo del TextField
                    unfocusedBorderColor = Color(0xFFBFAB92), // Color del borde cuando no está enfocado
                    unfocusedLabelColor = Color(0xFF755A3F) // Color del label cuando no está enfocado
                ),
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )



            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { username = it },
                label = { Text("Pasahitza") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF8F3E9), // Color del fondo del TextField
                    focusedBorderColor = Color(0xFF69472C), // Color del borde cuando está enfocado
                    unfocusedBorderColor = Color(0xFFBFAB92), // Color del borde cuando no está enfocado
                    cursorColor = Color(0xFF69472C), // Color del cursor
                    focusedLabelColor = Color(0xFF69472C), // Color del label cuando está enfocado
                    unfocusedLabelColor = Color(0xFF755A3F) // Color del label cuando no está enfocado
                ),
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )


            // Botón de Inicio de Sesión
            Button(
                onClick = {
                    // Aquí iría la lógica de autenticación
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(horizontal = 100.dp) // Ajusta el ancho del botón
                    .height(50.dp)
            ) {
                Text("Saioa hasi", color = hintColor, fontSize = 16.sp) // Aumenté el tamaño de la letra
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}