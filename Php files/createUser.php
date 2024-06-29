<?php
require_once 'conx.php';
$id=$_GET['id'];
$pass=$_GET['pass'];
$name = $_GET['name'];
$email=$_GET['email'];
$phone=$_GET['phone'];
$type=$_GET['type'];
$q="Select *from users where name='$name'";
$result = mysqli_query($con,$q);
if(mysqli_num_rows($result)==0){
    $query ="INSERT INTO users(id,password,type,name,email,phone) VALUES('$id','$pass','$type','$name','$email','$phone')";
	if(mysqli_query($con,$query))echo "success";
	}
else echo"exist";
?>