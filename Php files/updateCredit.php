<?php
require_once 'conx.php';
$maxcredit = $_GET['maxCredit'];
$id = $_GET['id'];
$semester=$_GET['semester'];
$query="UPDATE student SET maxcredit='$maxcredit' WHERE id='$id' and semester='$semester'";
if(mysqli_query($con, $query))
	echo "success";
?>