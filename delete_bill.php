<?php
$conn = new mysqli("localhost", "root", "", "split_itdb");

$bill_id = $_POST['id'];

$sql = "UPDATE bills SET status = 1 WHERE id = '$bill_id'";

if ($conn->query($sql) === TRUE) {
    echo "Archived Successfully";
} else {
    echo "Error: " . $conn->error;
}
$conn->close();
?>