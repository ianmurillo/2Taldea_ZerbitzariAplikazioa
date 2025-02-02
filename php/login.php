<?php
$host = "localhost"; // Cambia esto según sea necesario
$username = "root"; // Tu usuario de MySQL
$password = "abc123ABC"; // Tu contraseña de MySQL
$dbname = "2taldea"; // Tu base de datos

$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Recibir datos del cliente
$usuario = $_POST['izena'];
$pass = $_POST['pasahitza'];

// Consulta a la base de datos
$query = "SELECT * FROM langilea WHERE izena='$usuario' AND pasahitza='$pass'";
$result = $conn->query($query);

if ($result->num_rows > 0) {
    echo "success"; // Si se encuentra el usuario
} else {
    echo "failure"; // Si no se encuentra el usuario
}

$conn->close();
?>
