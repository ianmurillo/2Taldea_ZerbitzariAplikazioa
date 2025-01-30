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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.lazy.LazyColumn
import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.IOException
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Inicializar el navController para la navegación
            val navController = rememberNavController()

            // Definir las pantallas y la navegación
            NavHost(navController = navController, startDestination = "login_screen") {
                composable("login_screen") {
                    val context = LocalContext.current
                    LoginScreen(navController = navController, context = context)
                }

                // Pantalla principal después de iniciar sesión
                composable("main_screen/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    MainScreen(navController = navController, username = username)
                }
                composable("chat_screen/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    ChatScreen(navController = navController, username = username)
                }
                composable("eskaera_mesa_screen/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    EskaeraMesaScreen(navController = navController, username = username)
                }
                composable("pedido_mesa_screen/{username}/{mesaId}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val mesaId = backStackEntry.arguments?.getString("mesaId")?.toIntOrNull() ?: 0
                    PedidoMesaScreen(navController = navController, username = username, mesaId = mesaId)
                }
                composable("mesa_screen/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    MesaScreen(navController = navController, username = username)
                }
                composable("bebida_screen/{username}/{mesa}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val mesa = backStackEntry.arguments?.getString("mesa") ?: ""
                    BebidaScreen(navController = navController, username = username, mesa = mesa)
                }
                composable("primerosPlatosScreen/{username}/{mesa}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val mesa = backStackEntry.arguments?.getString("mesa") ?: ""
                    PrimerosPlatosScreen(navController = navController, username = username, mesa = mesa)
                }
                composable("segundosPlatosScreen/{username}/{mesa}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val mesa = backStackEntry.arguments?.getString("mesa") ?: ""
                    SegundosPlatosScreen(navController = navController, username = username, mesa = mesa)
                }
                composable("resumenPedidoScreen/{username}/{mesaId}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val mesaId = backStackEntry.arguments?.getString("mesaId")?.toIntOrNull() ?: 0

                    var pedido by remember { mutableStateOf(emptyList<Pair<String, Double>>()) }
                    var precioTotal by remember { mutableStateOf(0.0) }

                    // Obtener el pedido (puedes hacer esto de otra manera según cómo lo pases)
                    LaunchedEffect(Unit) {
                        // Aquí solo simulamos que tienes los pedidos en una lista (puedes obtenerlos de una API o de un estado anterior)
                        pedido = listOf(
                            "Plato 1" to 10.0,
                            "Plato 2" to 20.0,
                            "Bebida 1" to 5.0
                        )
                        precioTotal = pedido.sumOf { it.second }
                    }

                    // Navegar a la pantalla de resumen
                    ResumenPedidoScreen(
                        navController = navController,
                        pedido = pedido,
                        precioTotal = precioTotal,
                        mesaId = mesaId
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, context: Context) {
    // Colores de la paleta
    val backgroundColor = Color(0xFFBFAB92)
    val textColor = Color(0xFF1C1107)
    val buttonColor = Color(0xFF69472C)
    val hintColor = Color(0xFFF8F3E9)

    // Definición de las variables de estado
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Obtén el alcance de la corutina (esto te permite hacer operaciones asíncronas)
    val coroutineScope = rememberCoroutineScope()

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
                visualTransformation = PasswordVisualTransformation(), // Enmascara la contraseña
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Mostrar error si los campos están vacíos
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            // Botón de Inicio de Sesión
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        loading = true
                        coroutineScope.launch {
                            val url = "http://10.0.2.2/login.php" // URL local del servidor PHP
                            val requestQueue = Volley.newRequestQueue(context)
                            val stringRequest = object : StringRequest(
                                Request.Method.POST, url,
                                { response ->
                                    if (response.trim() == "success") {
                                        loading = false
                                        errorMessage = ""
                                        navController.navigate("main_screen/$username") // Navegación al éxito
                                    } else {
                                        loading = false
                                        errorMessage = "Usuario o contraseña incorrectos." // Mostrar mensaje del servidor
                                    }
                                },
                                { error ->
                                    loading = false
                                    errorMessage = "Error de conexión con el servidor." // Error de red
                                }
                            ) {
                                override fun getParams(): Map<String, String> {
                                    return mapOf(
                                        "izena" to username,    // Usar 'izena' para enviar el nombre de usuario
                                        "pasahitza" to password // Usar 'pasahitza' para enviar la contraseña
                                    )
                                }
                            }

                            requestQueue.add(stringRequest)
                        }
                    } else {
                        errorMessage = "Por favor, ingrese un nombre de usuario y una contraseña."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .padding(horizontal = 100.dp)
                    .height(50.dp)
            ) {
                Text("Saioa hasi", color = hintColor, fontSize = 16.sp)
            }

            // Mostrar indicador de carga mientras se hace la solicitud
            if (loading) {
                CircularProgressIndicator(color = buttonColor)
            }
        }
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
            // Barra superior con logo y nombre de usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_michisuji), // Asegúrate de usar tu logo
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = username, // Mostramos el nombre de usuario aquí
                    color = Color(0xFF1C1107),
                    fontSize = 20.sp
                )
            }

            // Opciones del menú principal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                listOf("Komandak", "Eskaerak", "Txata").forEach { label ->
                    Button(
                        onClick = {
                            when (label) {
                                "Komandak" -> {
                                    navController.navigate("mesa_screen/$username") // Navegar a pantalla de "Komandak" pasando el nombre de usuario
                                }
                                "Eskaerak" -> {
                                    navController.navigate("eskaera_mesa_screen/$username")
                                }
                                "Txata" -> {
                                    navController.navigate("chat_screen") // Navegar a la pantalla de "Txata"
                                }
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

        // Botón de cerrar sesión
        Button(
            onClick = {
                navController.navigate("login_screen") {
                    // Borramos las pantallas anteriores de la pila para que no se pueda volver al MainScreen
                    popUpTo("login_screen") { inclusive = true }
                }
            },
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
fun EskaeraMesaScreen(navController: NavHostController, username: String) {
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
                text = username, // Mostrar el nombre de usuario
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
                            onClick = {
                                // Navegación a la pantalla de PedidoMesaScreen con el número de mesa
                                navController.navigate("pedido_mesa_screen/$username/Mesa$tableNumber")
                            },
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
            onClick = { navController.popBackStack() }, // Volver a la pantalla anterior (MainScreen)
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


@Composable
fun PedidoMesaScreen(
    navController: NavHostController,
    username: String,
    mesaId: Int, // Cambiar tipo a Int
    pedido: List<Pair<String, Double>> = emptyList()  // Recibir los datos de los pedidos como parámetro
) {
    val backgroundColor = Color(0xFFBFAB92)
    val textColor = Color(0xFFF8F3E9)
    val precioTotal = pedido.sumOf { (_, precio) -> precio }  // Calcular el precio total directamente

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado: Usuario y mesa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mesa: $mesaId",
                    color = Color(0xFF1C1107),
                    fontSize = 20.sp
                )
                Text(
                    text = username,
                    color = Color(0xFF1C1107),
                    fontSize = 16.sp
                )
            }

            // Título
            Text(
                text = "Eskaeraren Laburpena",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Mostrar los pedidos
            pedido.forEach { (plato, precio) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(plato, color = textColor, fontSize = 18.sp)
                    Text(String.format("%.2f €", precio), color = textColor, fontSize = 18.sp)
                }
            }

            // Total
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Prezio Guztira: ${String.format("%.2f €", precioTotal)}",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Botón Atzera
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text("Atzera", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MesaScreen(navController: NavHostController, username: String) {
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
                text = username, // Mostrar el nombre de usuario
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
                            onClick = {
                                navController.navigate("bebida_screen/${username}/Mesa $tableNumber")
                            },
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
            onClick = { navController.popBackStack() }, // Volver a la pantalla anterior (MainScreen)
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

class BebidaViewModel : ViewModel() {
    // Estado de las bebidas seleccionadas
    var bebidas = mutableStateOf(
        mapOf(
            "Ura" to 0,
            "Koka-Kola" to 0,
            "Garagardoa" to 0,
            "Laranja Zukua" to 0
        )
    )

    // Función para actualizar la cantidad de una bebida
    fun updateBebida(bebida: String, cantidad: Int) {
        bebidas.value = bebidas.value.toMutableMap().apply {
            this[bebida] = cantidad
        }
    }

    // Función para obtener las bebidas seleccionadas
    fun getBebidasSeleccionadas(): List<Pair<String, Int>> {
        return bebidas.value.filter { it.value > 0 }
            .map { it.key to it.value }
    }
}

@Composable
fun BebidaScreen(navController: NavHostController, username: String, mesa: String) {
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    val bebidaViewModel: BebidaViewModel = viewModel()
    val bebidas = bebidaViewModel.bebidas.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Fila superior: logo y nombre del usuario y mesa
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
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = username, color = Color(0xFF1C1107), fontSize = 20.sp)
                    Text(text = mesa, color = Color(0xFF1C1107), fontSize = 16.sp)
                }
            }

            // Título
            Text(
                text = "Edaria",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Espaciador para centrar mejor las bebidas
            Spacer(modifier = Modifier.height(50.dp))

            // Botones para las bebidas
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Fila 1: Ura y Koka-Kola
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    BebidaCard("Ura", bebidas["Ura"] ?: 0, {
                        if (bebidas["Ura"]!! > 0) bebidaViewModel.updateBebida("Ura", bebidas["Ura"]!! - 1)
                    }, {
                        bebidaViewModel.updateBebida("Ura", bebidas["Ura"]!! + 1)
                    }, buttonColor, textColor)
                    BebidaCard("Koka-Kola", bebidas["Koka-Kola"] ?: 0, {
                        if (bebidas["Koka-Kola"]!! > 0) bebidaViewModel.updateBebida("Koka-Kola", bebidas["Koka-Kola"]!! - 1)
                    }, {
                        bebidaViewModel.updateBebida("Koka-Kola", bebidas["Koka-Kola"]!! + 1)
                    }, buttonColor, textColor)
                }
                // Fila 2: Garagardoa y Laranja Zukua
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    BebidaCard("Garagardoa", bebidas["Garagardoa"] ?: 0, {
                        if (bebidas["Garagardoa"]!! > 0) bebidaViewModel.updateBebida("Garagardoa", bebidas["Garagardoa"]!! - 1)
                    }, {
                        bebidaViewModel.updateBebida("Garagardoa", bebidas["Garagardoa"]!! + 1)
                    }, buttonColor, textColor)
                    BebidaCard("Laranja Zukua", bebidas["Laranja Zukua"] ?: 0, {
                        if (bebidas["Laranja Zukua"]!! > 0) bebidaViewModel.updateBebida("Laranja Zukua", bebidas["Laranja Zukua"]!! - 1)
                    }, {
                        bebidaViewModel.updateBebida("Laranja Zukua", bebidas["Laranja Zukua"]!! + 1)
                    }, buttonColor, textColor)
                }
            }

            // Botones "Atzera" y "Hurrengoa" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 200.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("mesa_screen") { launchSingleTop = true } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        // Pasar las bebidas seleccionadas a la siguiente pantalla
                        val bebidasSeleccionadas = bebidaViewModel.getBebidasSeleccionadas()
                        navController.navigate("primerosPlatosScreen/$username/$mesa/${bebidasSeleccionadas.joinToString(",")}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF69472C)),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hurrengoa", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun BebidaCard(
    nombre: String,
    cantidad: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    buttonColor: Color,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(150.dp)
    ) {
        // Nombre de la bebida dentro de un cuadro marrón más pequeño
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .background(buttonColor, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = nombre,
                color = textColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        // Botones de aumentar y disminuir cantidad
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Button(
                onClick = { onDecrease() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("-", color = Color.Black, fontSize = 20.sp)
            }
            Text("$cantidad", color = Color.Black, fontSize = 20.sp)
            Button(
                onClick = { onIncrease() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", color = Color.Black, fontSize = 20.sp)
            }
        }
    }
}

class PrimerosPlatosViewModel : ViewModel() {
    // Variables para las cantidades de cada plato
    var cantidadBarazkiZopa by mutableStateOf(0)
    var cantidadZesarEntsalada by mutableStateOf(0)
    var cantidadGazpatxoa by mutableStateOf(0)
    var cantidadKalabazaKrema by mutableStateOf(0)

    // Funciones para modificar las cantidades
    fun increaseBarazkiZopa() {
        cantidadBarazkiZopa++
    }

    fun decreaseBarazkiZopa() {
        if (cantidadBarazkiZopa > 0) cantidadBarazkiZopa--
    }

    fun increaseZesarEntsalada() {
        cantidadZesarEntsalada++
    }

    fun decreaseZesarEntsalada() {
        if (cantidadZesarEntsalada > 0) cantidadZesarEntsalada--
    }

    fun increaseGazpatxoa() {
        cantidadGazpatxoa++
    }

    fun decreaseGazpatxoa() {
        if (cantidadGazpatxoa > 0) cantidadGazpatxoa--
    }

    fun increaseKalabazaKrema() {
        cantidadKalabazaKrema++
    }

    fun decreaseKalabazaKrema() {
        if (cantidadKalabazaKrema > 0) cantidadKalabazaKrema--
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimerosPlatosScreen(navController: NavHostController, username: String, mesa: String, viewModel: PrimerosPlatosViewModel = viewModel()) {
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fila superior: logo y nombre del usuario y mesa
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
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = username,
                        color = Color(0xFF1C1107),
                        fontSize = 20.sp
                    )
                    Text(
                        text = mesa,
                        color = Color(0xFF1C1107),
                        fontSize = 16.sp
                    )
                }
            }

            // Título
            Text(
                text = "Lehen Platerak",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Espaciador para centrar mejor los platos
            Spacer(modifier = Modifier.height(50.dp))

            // Botones para los platos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Fila 1: Barazki Zopa y Zesar Entsalada
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard(
                        "Barazki Zopa",
                        viewModel.cantidadBarazkiZopa,
                        { viewModel.decreaseBarazkiZopa() },
                        { viewModel.increaseBarazkiZopa() },
                        buttonColor,
                        textColor
                    )
                    PlatoCard(
                        "Zesar Entsalada",
                        viewModel.cantidadZesarEntsalada,
                        { viewModel.decreaseZesarEntsalada() },
                        { viewModel.increaseZesarEntsalada() },
                        buttonColor,
                        textColor
                    )
                }
                // Fila 2: Gazpatxoa y Kalabaza Krema
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard(
                        "Gazpatxoa",
                        viewModel.cantidadGazpatxoa,
                        { viewModel.decreaseGazpatxoa() },
                        { viewModel.increaseGazpatxoa() },
                        buttonColor,
                        textColor
                    )
                    PlatoCard(
                        "Kalabaza Krema",
                        viewModel.cantidadKalabazaKrema,
                        { viewModel.decreaseKalabazaKrema() },
                        { viewModel.increaseKalabazaKrema() },
                        buttonColor,
                        textColor
                    )
                }
            }

            // Espaciador flexible para empujar los botones hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botones "Atzera" y "Hurrengoa" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("bebida_screen") { launchSingleTop = true } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        navController.navigate("segundosPlatosScreen/$username/$mesa")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hurrengoa", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun PlatoCard(
    nombre: String,
    cantidad: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    buttonColor: Color,
    textColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.size(150.dp)
    ) {
        // Nombre del plato dentro de un cuadro marrón más pequeño
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .background(buttonColor, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(nombre, color = textColor, fontSize = 14.sp, textAlign = TextAlign.Center)
        }

        // Botones de aumentar y disminuir cantidad
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Button(
                onClick = { onDecrease() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("-", color = Color.Black, fontSize = 20.sp)
            }
            Text("$cantidad", color = Color.Black, fontSize = 20.sp)
            Button(
                onClick = { onIncrease() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("+", color = Color.Black, fontSize = 20.sp)
            }
        }
    }
}

class SegundosPlatosViewModel : ViewModel() {
    // Variables para las cantidades de cada plato
    var cantidadLabekoOilaskoa by mutableStateOf(0)
    var cantidadLegatzaPlantxan by mutableStateOf(0)
    var cantidadBeheikiXerra by mutableStateOf(0)
    var cantidadBarazkiLasagna by mutableStateOf(0)

    // Funciones para modificar las cantidades
    fun increaseLabekoOilaskoa() {
        cantidadLabekoOilaskoa++
    }

    fun decreaseLabekoOilaskoa() {
        if (cantidadLabekoOilaskoa > 0) cantidadLabekoOilaskoa--
    }

    fun increaseLegatzaPlantxan() {
        cantidadLegatzaPlantxan++
    }

    fun decreaseLegatzaPlantxan() {
        if (cantidadLegatzaPlantxan > 0) cantidadLegatzaPlantxan--
    }

    fun increaseBeheikiXerra() {
        cantidadBeheikiXerra++
    }

    fun decreaseBeheikiXerra() {
        if (cantidadBeheikiXerra > 0) cantidadBeheikiXerra--
    }

    fun increaseBarazkiLasagna() {
        cantidadBarazkiLasagna++
    }

    fun decreaseBarazkiLasagna() {
        if (cantidadBarazkiLasagna > 0) cantidadBarazkiLasagna--
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegundosPlatosScreen(
    navController: NavHostController,
    username: String,
    mesa: String,
    viewModel: SegundosPlatosViewModel = viewModel()
) {
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fila superior: logo y nombre del usuario y mesa
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
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = username,
                        color = Color(0xFF1C1107),
                        fontSize = 20.sp
                    )
                    Text(
                        text = mesa,
                        color = Color(0xFF1C1107),
                        fontSize = 16.sp
                    )
                }
            }

            // Título
            Text(
                text = "Bigarren Platerak",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Espaciador para centrar mejor los platos
            Spacer(modifier = Modifier.height(50.dp))

            // Botones para los platos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Fila 1: Labeko Oilaskoa y Legatza Plantxan
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard(
                        "Labeko Oilaskoa",
                        viewModel.cantidadLabekoOilaskoa,
                        { viewModel.decreaseLabekoOilaskoa() },
                        { viewModel.increaseLabekoOilaskoa() },
                        buttonColor,
                        textColor
                    )
                    PlatoCard(
                        "Legatza Plantxan",
                        viewModel.cantidadLegatzaPlantxan,
                        { viewModel.decreaseLegatzaPlantxan() },
                        { viewModel.increaseLegatzaPlantxan() },
                        buttonColor,
                        textColor
                    )
                }
                // Fila 2: Beheiki Xerra y Barazki Lasagna
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard(
                        "Beheiki Xerra",
                        viewModel.cantidadBeheikiXerra,
                        { viewModel.decreaseBeheikiXerra() },
                        { viewModel.increaseBeheikiXerra() },
                        buttonColor,
                        textColor
                    )
                    PlatoCard(
                        "Barazki Lasagna",
                        viewModel.cantidadBarazkiLasagna,
                        { viewModel.decreaseBarazkiLasagna() },
                        { viewModel.increaseBarazkiLasagna() },
                        buttonColor,
                        textColor
                    )
                }
            }

            // Espaciador flexible para empujar los botones hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botones "Atzera" y "Eskaera Ikusi" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() }, // Volver a PrimerosPlatosScreen
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        navController.navigate(
                            "resumenPedidoScreen/${username}/${mesa}"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Eskaera Ikusi", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

class PedidoViewModel : ViewModel() {
    // Guardar los platos seleccionados (nombre, cantidad, precio)
    private val _pedido = mutableStateOf<MutableList<Pair<String, Double>>>(mutableListOf())
    val pedido: State<List<Pair<String, Double>>> = _pedido

    // Añadir un plato o bebida al pedido
    fun addToPedido(plato: String, precio: Double) {
        _pedido.value.add(plato to precio)
    }

    // Obtener el precio total
    fun calcularPrecioTotal(): Double {
        return _pedido.value.sumOf { it.second }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenPedidoScreen(
    navController: NavHostController,
    pedido: List<Pair<String, Double>>, // Aquí ya estás recibiendo la lista de platos y precios
    precioTotal: Double,
    mesaId: Int
) {
    // Colores
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    // Estado para mostrar el indicador de carga
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fila superior: logo y nombre del usuario y mesa
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
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Mahaia: $mesaId",
                        color = Color(0xFF1C1107),
                        fontSize = 20.sp
                    )
                }
            }

            // Título
            Text(
                text = "Laburpena",
                color = textColor,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Contenedor de los detalles del pedido
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(buttonColor, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // Listado de los platos y bebidas
                    pedido.forEach { (plato, precio) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = plato,
                                color = textColor,
                                fontSize = 18.sp
                            )
                            Text(
                                text = String.format("%.2f€", precio),
                                color = textColor,
                                fontSize = 18.sp
                            )
                        }
                        Divider(color = textColor, thickness = 1.dp)
                    }

                    // Precio total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Prezioa",
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format("%.2f€", precioTotal),
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botones "Atzera" y "Eskaera Sortu" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        loading = true
                        coroutineScope.launch {
                            try {
                                val url = "http://10.0.2.2/guardar_pedido.php" // Cambia por tu URL
                                val requestQueue = Volley.newRequestQueue(navController.context)

                                pedido.forEachIndexed { index, (plato, precio) ->
                                    val stringRequest = object : StringRequest(
                                        Request.Method.POST, url,
                                        { response ->
                                            if (response.trim() != "success") {
                                                errorMessage = "Error al guardar el pedido: $response"
                                                loading = false
                                            }

                                            // Si es el último elemento y no hay errores, navega
                                            if (index == pedido.size - 1 && errorMessage.isEmpty()) {
                                                loading = false
                                                navController.navigate("main_screen") {
                                                    popUpTo("main_screen") { inclusive = true }
                                                }
                                            }
                                        },
                                        { error ->
                                            errorMessage = "Error de red: ${error.message}"
                                            loading = false
                                        }
                                    ) {
                                        override fun getParams(): Map<String, String> {
                                            return mapOf(
                                                "eskaeraZenb" to "1234", // Genera un número único si es necesario
                                                "izena" to plato,
                                                "prezioa" to precio.toString(),
                                                "mesa_id" to mesaId.toString()
                                            )
                                        }
                                    }
                                    requestQueue.add(stringRequest)
                                }
                            } catch (e: Exception) {
                                loading = false
                                errorMessage = "Error inesperado: ${e.message}"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Eskaera Sortu", color = Color.White, fontSize = 16.sp)
                }
            }

            // Mostrar indicador de carga o mensaje de error
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


class ChatViewModel : ViewModel() {
    private var socket: Socket? = null
    private var out: PrintWriter? = null
    private var inStream: BufferedReader? = null

    private val _messages = MutableStateFlow<List<Pair<String, Boolean>>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    fun connectToServer(serverIp: String = "192.168.115.158", port: Int = 5555) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                socket = Socket(serverIp, port).apply {
                    soTimeout = 5000 // Tiempo de espera de 5 segundos para evitar bloqueos
                }
                out = PrintWriter(socket!!.getOutputStream(), true)
                inStream = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                _connectionState.value = true

                while (true) {
                    val message = inStream?.readLine() ?: break
                    if (message.isNotEmpty()) {
                        _messages.value = _messages.value + Pair(message, false)
                    }
                }
            } catch (e: IOException) {
                Log.e("ChatViewModel", "Error de conexión: ${e.message}")
                _connectionState.value = false
            }
        }
    }

    fun sendMessage(user: String, message: String) {
        if (message.isNotBlank() && _connectionState.value) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    out?.println("$user: $message")
                    _messages.value = _messages.value + Pair("$user: $message", true)
                } catch (e: IOException) {
                    Log.e("ChatViewModel", "Error enviando mensaje: ${e.message}")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            socket?.close()
            out?.close()
            inStream?.close()
        } catch (e: IOException) {
            Log.e("ChatViewModel", "Error cerrando conexión: ${e.message}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    username: String, // Aquí agregamos el parámetro username
    viewModel: ChatViewModel = viewModel() // Obtener el ViewModel
) {
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }

    val backgroundColor = Color(0xFFBFAB92)
    val bubbleColorSender = Color(0xFF69472C)
    val bubbleColorReceiver = Color(0xFFF8F3E9)
    val textColorSender = Color(0xFFF8F3E9)
    val textColorReceiver = Color(0xFF1C1107)

    LaunchedEffect(Unit) {
        viewModel.connectToServer()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barra superior con el logo a la izquierda, el título centrado y el botón "Atzera" a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Esto asegura que los elementos estén en los extremos
            ) {
                // Logo en la izquierda
                Image(
                    painter = painterResource(id = R.drawable.logo_michisuji), // Asegúrate de tener un logo en res/drawable
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp) // Ajusta el tamaño del logo según necesites
                )

                // Título "Chat" centrado
                Text(
                    text = "Txata",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f), // Esto empuja el título al centro
                    textAlign = TextAlign.Center
                )

                // Botón "Atzera" a la derecha
                Button(
                    onClick = { navController.navigate("main_screen") }, // Navega a MainScreen
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF69472C)
                    )
                ) {
                    Text("Atzera", color = Color.White)
                }
            }

            // Lista de mensajes
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(messages) { mensaje ->
                    ChatBubble(
                        mensaje = mensaje.first,
                        isSender = mensaje.second,
                        bubbleColorSender = bubbleColorSender,
                        bubbleColorReceiver = bubbleColorReceiver,
                        textColorSender = textColorSender,
                        textColorReceiver = textColorReceiver
                    )
                }
            }

            // Campo de texto para ingresar nuevos mensajes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF69472C), shape = RoundedCornerShape(16.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Idatzi mezua...") },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(color = Color.White)
                )
                IconButton(onClick = {
                    viewModel.sendMessage(username, messageText) // Usa el username aquí para enviar el mensaje
                    messageText = ""
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar", tint = Color.Green)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    mensaje: String,
    isSender: Boolean,
    bubbleColorSender: Color,
    bubbleColorReceiver: Color,
    textColorSender: Color,
    textColorReceiver: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSender) bubbleColorSender else bubbleColorReceiver,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 300.dp)
        ) {
            Text(
                text = mensaje,
                color = if (isSender) textColorSender else textColorReceiver,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()

    // Simulamos el contexto en la vista previa
    val context = LocalContext.current

    // Pasamos el contexto simulado al Composable
    LoginScreen(navController = navController, context = context)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController, username = "Jon")
}

@Preview(showBackground = true)
@Composable
fun PreviewEskaeraMesaScreen() {
    val navController = rememberNavController()
    EskaeraMesaScreen(navController = navController, username = "Jon")
}

@Preview(showBackground = true)
@Composable
fun MesaScreenPreview() {
    val navController = rememberNavController()
    // Simulamos el nombre de usuario que se pasa desde el Login
    val username = "Jon"
    MesaScreen(navController = navController, username = username)
}

@Preview(showBackground = true)
@Composable
fun BebidaScreenPreview() {
    val navController = rememberNavController()
    // Simulamos el nombre de usuario y el número de mesa seleccionada
    val username = "Jon"
    val mesa = "Mesa 1"  // La mesa elegida por el usuario
    BebidaScreen(navController = navController, username = username, mesa = mesa)
}

@Preview(showBackground = true)
@Composable
fun PrimerosScreenPreview() {
    // Fake NavController para el Preview
    val fakeNavController = rememberNavController()

    // Llamamos a la función principal con datos ficticios
    PrimerosPlatosScreen(
        navController = fakeNavController,
        username = "Jon",
        mesa = "Mahai 1"
    )
}

@Preview(showBackground = true)
@Composable
fun SegundosScreenPreview() {
    // Fake NavController para el Preview
    val fakeNavController = rememberNavController()

    // Llamamos a la función principal con datos ficticios
    SegundosPlatosScreen(
        navController = fakeNavController,
        username = "Jon",
        mesa = "Mahai 1"
    )
}

@Preview(showBackground = true)
@Composable
fun ResumenPedidoPreview() {
    // Simulación de datos de ejemplo
    val pedidoEjemplo = listOf(
        "Ura" to 1.5,
        "Koka-Kola" to 2.0,
        "Zesar Entsalada" to 5.99,
        "Gazpatxoa" to 3.99,
        "Labeko Oilaskoa" to 10.99,
        "Beheiki Xerra" to 15.99,
        "Barazki Lasagna" to 11.99
    )
    val precioTotalEjemplo = pedidoEjemplo.sumOf { (_, cantidad) -> cantidad } // Precio ficticio calculado
    val mesaIdEjemplo = 1 // Ejemplo de ID de la mesa

    // Agregar un contenedor de tema si se necesita
    MaterialTheme {
        ResumenPedidoScreen(
            navController = rememberNavController(), // Proveer un NavController ficticio
            pedido = pedidoEjemplo,
            precioTotal = precioTotalEjemplo,
            mesaId = mesaIdEjemplo // Pasar el ID de la mesa al Composable
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    val navController = rememberNavController()

    // Simulando algunos mensajes de ejemplo
    val mensajes = listOf(
        Pair("Kaixo!", true),
        Pair("Kaixo, zer moduz?", false),
        Pair("Ondo, eskerrik asko", true),
        Pair("Pozten naiz", false)
    )

    // Usar un nombre de usuario de ejemplo
    val username = "usuarioEjemplo"

    // Pasar el username junto con el navController y viewModel
    ChatScreen(
        navController = navController,
        username = username,  // Asegúrate de pasar el parámetro 'username'
        viewModel = viewModel() // Asegúrate de que el ViewModel se pase
    )
}




