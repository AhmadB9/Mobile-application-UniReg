<?php
require_once 'conx.php';
$id=$_GET['id'];
$certificate=$_GET['certificate'];
$query ="INSERT INTO instructor(id,certificates,feedback,rate,votersnb,performance) VALUES('$id','$certificate','null','0','0','null')";
if(mysqli_query($con,$query))echo "success";
?>