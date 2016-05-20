<?php
$error=''; // Variable To Store Error Message
if (isset($_POST['submit'])) {
if (empty($_POST['username']) || empty($_POST['password'])) {
$error = "Username or Password is invalid";
}
else
{
// Define $username and $password
$username=$_POST['username'];
$password=$_POST['password'];

if ($username === "hazem" && $password === "12345") {
header("location: profile.php"); // Redirecting To Other Page
} else {
$error = "Username or Password is invalid";
}
}
}
?>
