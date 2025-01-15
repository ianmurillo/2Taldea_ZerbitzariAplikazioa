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
import androidx.compose.ui.input.key.Key.Companion
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
import androidx.compose.foundation.lazy.items


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") { LoginScreen(navController = navController) }
        composable("main_screen/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MainScreen(navController = navController, username = username)
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
        composable("resumenPedidoScreen/{username}/{mesa}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val mesa = backStackEntry.arguments?.getString("mesa") ?: ""
            val pedido = listOf( // Ejemplo de pedido, reemplazar con los datos reales
                "Labeko Oilaskoa" to 2,
                "Legatza Plantxan" to 1
            )
            val precioTotal = pedido.sumOf { (_, cantidad) -> cantidad * 10.0 } // Precio ficticio

            ResumenPedidoScreen(
                navController = navController,
                pedido = pedido,
                precioTotal = precioTotal
            )
        }
        composable("txata_screen") {
            // Aquí va tu pantalla de txata_screen
        }
        composable("chat_screen") { backStackEntry ->
            ChatScreen(
                navController = navController,
                mensajes = listOf(Pair("Hola!", true), Pair("¿Cómo estás?", false))
            )
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

    // Definición de las variables de estado
        var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

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

            // Mostrar error si los campos están vacíos
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            // Botón de Inicio de Sesión
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        navController.navigate("main_screen/$username")
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
                                    // Lógica para la pantalla de "Eskaerak", si la tienes configurada
                                    // Por ejemplo: navController.navigate("eskaerak_screen")
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




@Composable
fun BebidaScreen(navController: NavHostController, username: String, mesa: String) {
    // Colores
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    // Cantidades iniciales de cada bebida
    var cantidadUra by remember { mutableStateOf(0) }
    var cantidadKokaKola by remember { mutableStateOf(0) }
    var cantidadGaragardoa by remember { mutableStateOf(0) }
    var cantidadLaranjaZukua by remember { mutableStateOf(0) }

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
                    BebidaCard("Ura", cantidadUra, { if (cantidadUra > 0) cantidadUra-- }, { cantidadUra++ }, buttonColor, textColor)
                    BebidaCard("Koka-Kola", cantidadKokaKola, { if (cantidadKokaKola > 0) cantidadKokaKola-- }, { cantidadKokaKola++ }, buttonColor, textColor)
                }
                // Fila 2: Garagardoa y Laranja Zukua
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    BebidaCard("Garagardoa", cantidadGaragardoa, { if (cantidadGaragardoa > 0) cantidadGaragardoa-- }, { cantidadGaragardoa++ }, buttonColor, textColor)
                    BebidaCard("Laranja Zukua", cantidadLaranjaZukua, { if (cantidadLaranjaZukua > 0) cantidadLaranjaZukua-- }, { cantidadLaranjaZukua++ }, buttonColor, textColor)
                }
            }

            // Botones "Atzera" y "Hurrengoa" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 200.dp), // Ajuste para centrar mejor los botones
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("mesaScreen") { launchSingleTop = true } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        navController.navigate("primerosPlatos/$username/$mesa")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimerosPlatosScreen(navController: NavHostController, username: String, mesa: String) {
    // Colores
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    // Cantidades iniciales de cada plato
    var cantidadBarazkiZopa by remember { mutableStateOf(0) }
    var cantidadZesarEntsalada by remember { mutableStateOf(0) }
    var cantidadGazpatxoa by remember { mutableStateOf(0) }
    var cantidadKalabazaKrema by remember { mutableStateOf(0) }

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
                    PlatoCard("Barazki Zopa", cantidadBarazkiZopa, { if (cantidadBarazkiZopa > 0) cantidadBarazkiZopa-- }, { cantidadBarazkiZopa++ }, buttonColor, textColor)
                    PlatoCard("Zesar Entsalada", cantidadZesarEntsalada, { if (cantidadZesarEntsalada > 0) cantidadZesarEntsalada-- }, { cantidadZesarEntsalada++ }, buttonColor, textColor)
                }
                // Fila 2: Gazpatxoa y Kalabaza Krema
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard("Gazpatxoa", cantidadGazpatxoa, { if (cantidadGazpatxoa > 0) cantidadGazpatxoa-- }, { cantidadGazpatxoa++ }, buttonColor, textColor)
                    PlatoCard("Kalabaza Krema", cantidadKalabazaKrema, { if (cantidadKalabazaKrema > 0) cantidadKalabazaKrema-- }, { cantidadKalabazaKrema++ }, buttonColor, textColor)
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
                    onClick = { navController.popBackStack() }, // Acción de "Atzera"
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
                    }, // Navegación hacia SegundosPlatosScreen
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegundosPlatosScreen(navController: NavHostController, username: String, mesa: String) {
    // Colores
    val backgroundColor = Color(0xFFBFAB92)
    val buttonColor = Color(0xFF69472C)
    val textColor = Color(0xFFF8F3E9)

    // Cantidades iniciales de cada plato
    var cantidadLabekoOilaskoa by remember { mutableStateOf(0) }
    var cantidadLegatzaPlantxan by remember { mutableStateOf(0) }
    var cantidadBeheikiXerra by remember { mutableStateOf(0) }
    var cantidadBarazkiLasagna by remember { mutableStateOf(0) }

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
                    PlatoCard("Labeko Oilaskoa", cantidadLabekoOilaskoa, { if (cantidadLabekoOilaskoa > 0) cantidadLabekoOilaskoa-- }, { cantidadLabekoOilaskoa++ }, buttonColor, textColor)
                    PlatoCard("Legatza Plantxan", cantidadLegatzaPlantxan, { if (cantidadLegatzaPlantxan > 0) cantidadLegatzaPlantxan-- }, { cantidadLegatzaPlantxan++ }, buttonColor, textColor)
                }
                // Fila 2: Beheiki Xerra y Barazki Lasagna
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlatoCard("Beheiki Xerra", cantidadBeheikiXerra, { if (cantidadBeheikiXerra > 0) cantidadBeheikiXerra-- }, { cantidadBeheikiXerra++ }, buttonColor, textColor)
                    PlatoCard("Barazki Lasagna", cantidadBarazkiLasagna, { if (cantidadBarazkiLasagna > 0) cantidadBarazkiLasagna-- }, { cantidadBarazkiLasagna++ }, buttonColor, textColor)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenPedidoScreen(navController: NavHostController, pedido: List<Pair<String, Int>>, precioTotal: Double) {
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
                        text = "Izena",
                        color = Color(0xFF1C1107),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Mahaia",
                        color = Color(0xFF1C1107),
                        fontSize = 16.sp
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
                    // Listado de los platos
                    pedido.forEach { (plato, cantidad) ->
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = cantidad.toString(),
                                    color = textColor,
                                    fontSize = 18.sp
                                )
                            }
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

            // Espaciador flexible para empujar los botones hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botones "Atzera" y "Eskaera Sortu" en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() }, // Acción de "Atzera"
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White, fontSize = 16.sp)
                }
                Button(
                    onClick = {
                        // Guardar pedido (puedes manejar esta lógica aquí o en un ViewModel)
                        navController.navigate("mainScreen") {
                            popUpTo("mainScreen") { inclusive = true } // Limpiar la pila de navegación
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Verde
                    modifier = Modifier
                        .size(width = 150.dp, height = 50.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Eskaera Sortu", color = Color.White, fontSize = 16.sp)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, mensajes: List<Pair<String, Boolean>>) {
    // Colores del diseño
    val backgroundColor = Color(0xFFBFAB92)
    val bubbleColorSender = Color(0xFF69472C)
    val bubbleColorReceiver = Color(0xFFF8F3E9)
    val textColorSender = Color(0xFFF8F3E9)
    val textColorReceiver = Color(0xFF1C1107)

    // Estado para el texto del mensaje
    var messageText by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf(mensajes) }

    // Función para enviar mensaje
    fun sendMessage() {
        if (messageText.isNotEmpty()) {
            chatMessages = listOf(Pair(messageText, true)) + chatMessages
            messageText = ""  // Limpiar el campo de texto después de enviar
        }
    }

    // Pantalla principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado del chat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_michisuji),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "Chat",
                    color = Color(0xFF1C1107),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { navController.popBackStack() }, // Regresar a MainScreen
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Atzera", color = Color.White)
                }
            }

            // Lista de mensajes
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                reverseLayout = true // Muestra los mensajes desde el final
            ) {
                items(chatMessages) { mensaje ->
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

            // Input de texto
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF69472C), shape = RoundedCornerShape(16.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Idatzi mezua...", color = Color(0xFFF8F3E9)) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { sendMessage() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Bidali",
                        tint = Color(0xFF4CAF50)
                    )
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
    // Simulamos el nombre de usuario que se pasa desde el Login
    val username = "Izena"
    MesaScreen(navController = navController, username = username)
}


@Preview(showBackground = true)
@Composable
fun BebidaScreenPreview() {
    val navController = rememberNavController()
    // Simulamos el nombre de usuario y el número de mesa seleccionada
    val username = "Izena"
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
        username = "Izena",
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
        username = "Izena",
        mesa = "Mahai 1"
    )
}

@Preview(showBackground = true,)
@Composable
fun ResumenPedidoPreview() {
    // Simulación de datos de ejemplo
    val pedidoEjemplo = listOf(
        "Ura" to 3,
        "Koka-Kola" to 1,
        "Zesar Entsalada" to 2,
        "Gazpatxoa" to 2,
        "Labeko Oilaskoa" to 1,
        "Beheiki Xerra" to 2,
        "Barazki Lasagna" to 1
    )
    val precioTotalEjemplo = 210.33

    // Llamada al Composable ResumenPedidoScreen con un NavHostController ficticio
    ResumenPedidoScreen(
        navController = rememberNavController(),
        pedido = pedidoEjemplo,
        precioTotal = precioTotalEjemplo
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    // Lista de mensajes inicial
    val mensajes = listOf(
        Pair("¡Hola!", true),
        Pair("Hola, ¿cómo estás?", false),
        Pair("Todo bien, gracias", true),
        Pair("Me alegro", false)
    )

    ChatScreen(
        navController = rememberNavController(),
        mensajes = mensajes
    )
}




