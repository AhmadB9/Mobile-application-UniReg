<?php
$server="localhost";
$user="root";
$password=""; // this field is equal to root for MAC users
$db = "data";
$con = mysqli_connect($server,$user,$password,$db);
if (mysqli_connect_errno())
{
echo mysqli_connect_error();
}

?>