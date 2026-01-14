<?php
$conn = new mysqli("localhost", "root", "", "split_itdb");

if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed"]));
}

$user_id = isset($_POST['user_id']) ? $_POST['user_id'] : '';

if (!empty($user_id)) {
    $sql = "SELECT fullname, email, birthdate FROM users WHERE id = '$user_id'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        echo json_encode($result->fetch_assoc());
    } else {
        echo json_encode(["error" => "User ID $user_id not found"]);
    }
} else {
    echo json_encode(["error" => "No ID provided"]);
}
$conn->close();
?>