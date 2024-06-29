<?php
require_once 'conx.php';
$id =$_GET['id'];
$query = "select name from users where id='$id'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	echo $row['name'];	
}}

?>