<?php
require_once 'conx.php';
$id = $_GET['id'];
$pass = $_GET['pass'];
$query = "select type from users where id='$id' and password='$pass'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)==1){
while ($row = mysqli_fetch_assoc($result)) {
	echo $row['type'];	
}}
else echo "empty" ;

?>