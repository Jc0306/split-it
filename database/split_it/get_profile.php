<?php
$host = "localhost";
$user = "root";
$pass = "";
$db   = "split_itdb"; // Ensure this matches your database name

$conn = mysqli_connect($host, $user, $pass, $db);

if (!$conn) {
    die(json_encode(["error" => "Connection failed: " . mysqli_connect_error()]));
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Check if user_id was sent from Android
    if (isset($_POST['user_id'])) {
        $userId = mysqli_real_escape_string($conn, $_POST['user_id']);

        // Query the database for this specific user
        $sql = "SELECT * FROM users WHERE id = '$userId'";
        $result = mysqli_query($conn, $sql);

        if (mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);

            // Send back JSON with keys that match your Java (optString/getString)
            echo json_encode([
                "status" => "success",
                "fullname" => $row['fullname'], // Ensure column name is 'fullname' in DB
                "email" => $row['email'],
                "birthdate" => $row['birthdate'],
                "password" => $row['password']
            ]);
        } else {
            echo json_encode(["error" => "User not found"]);
        }
    } else {
        echo json_encode(["error" => "No user_id provided"]);
    }
}

mysqli_close($conn);
?>