<?php
require_once 'conx.php';
$id = $_GET['id'];
$name = $_GET['name'];
$email=$_GET['email'];
$phone=$_GET['phone'];
$query = "select* from users where id='$id' and name='$name' and email='$email' and phone='$phone'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)==1){
echo "success";
}
else echo "empty"
?>