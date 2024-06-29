<?php
require_once 'conx.php';
$pass = $_GET['pass'];
$id = $_GET['id'];
$query="UPDATE users SET password='$pass' WHERE id='$id'";
if(mysqli_query($con, $query)){
echo "success";}
?>