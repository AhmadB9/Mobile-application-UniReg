<?php
require_once 'conx.php';
$id=$_GET['id'];
$status=$_GET['status'];
$maxcredit=$_GET['maxCredit'];
$semester = $_GET['semester'];
$query="UPDATE student SET status='$status',maxcredit='$maxcredit',semester='$semester' WHERE id='$id'";
if(mysqli_query($con, $query))echo "success";
?>