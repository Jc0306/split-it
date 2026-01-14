<?php
$host = "localhost";
$user = "root"; 
$pass = "";     
$db   = "split_itDB";

$conn = mysqli_connect($host, $user, $pass, $db);

if($conn) {
    echo "Success! Database is connected.";
} else {
    echo "Connection Failed.";
}
?>