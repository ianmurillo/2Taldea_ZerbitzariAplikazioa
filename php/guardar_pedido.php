<?php
header('Content-Type: application/json');

$servername = "localhost"; // Cambia esto por tu servidor MySQL
$username = "root"; // Cambia esto por tu usuario de MySQL
$password = "abc123ABC"; // Cambia esto por tu contraseña de MySQL
$dbname = "2taldea"; // Cambia esto por tu base de datos

// Conectar a la base de datos
$conn = new mysqli($servername, $username, $password, $dbname);

// Verificar la conexión
if ($conn->connect_error) {
    die(json_encode(["error" => "Conexión fallida: " . $conn->connect_error]));
}

// Verificar si se recibieron los datos correctos
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $eskaeraZenb = $_POST['eskaeraZenb'] ?? '';
    $izena = $_POST['izena'] ?? '';
    $prezioa = $_POST['prezioa'] ?? '';
    $mesa_id = $_POST['mesa_id'] ?? '';

    if (empty($eskaeraZenb) || empty($izena) || empty($prezioa) || empty($mesa_id)) {
        echo json_encode(["error" => "Faltan datos en la solicitud"]);
        exit();
    }

    // Insertar el pedido en la tabla eskaera
    $stmt = $conn->prepare("INSERT INTO eskaera (eskaeraZenb, izena, prezioa, mesa_id) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssdi", $eskaeraZenb, $izena, $prezioa, $mesa_id);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Pedido guardado correctamente"]);
    } else {
        echo json_encode(["error" => "Error al guardar el pedido: " . $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["error" => "Método no permitido"]);
}

$conn->close();
?>
