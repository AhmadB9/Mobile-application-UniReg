<?php
require_once 'conx.php';
$id=$_GET['id'];
$email=$_GET['email'];
$phone=$_GET['phone'];
$pass = $_GET['pass'];
$name=$_GET['name'];
$query="UPDATE users SET password='$pass',name='$name',email='$email',phone='$phone' WHERE id='$id'";
if(mysqli_query($con, $query))echo "success";
?>