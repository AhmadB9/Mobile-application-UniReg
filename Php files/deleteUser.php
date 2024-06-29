<?php
require_once 'conx.php';
$id=$_GET['id'];
$query="DELETE from users WHERE id='$id'";
if(mysqli_query($con, $query))echo "success";
?>