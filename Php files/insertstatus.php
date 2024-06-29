<?php
require_once 'conx.php';
$id=$_GET['id'];
$status=$_GET['status'];
$maxcredit=$_GET['maxCredit'];
$semester = $_GET['semester'];
$q="select * from student where ID='$id' and status='$status' and semester='$semester'";
$result = mysqli_query($con,$q);
if(mysqli_num_rows($result)>0)
	echo"exist";
else{
$query ="INSERT INTO student(ID,status,maxcredit,semester) VALUES('$id','$status','$maxcredit','$semester')";
if(mysqli_query($con,$query))echo "success";
}
?>