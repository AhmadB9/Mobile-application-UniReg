<?php
require_once 'conx.php';
$id=$_GET['id'];
$course=$_GET['course'];
$code=$_GET['code'];
$time=$_GET['time'];
$name=$_GET['instructor'];
$description=$_GET['description'];
$credit=$_GET['credit'];
$available=$_GET['available'];
$query="select * from courses where instructorID='$id' and coursename='$course' and time='$time'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0)
	echo "exist";
else {
	$query="select * from courses where instructorID='$id' and coursename!='$course' and time='$time'";
	$result = mysqli_query($con,$query);
	if(mysqli_num_rows($result)>0)
		echo "same time";
	else{
		$q ="INSERT INTO courses(coursename,instructorID,code,instructor,credit,time,available,description) VALUES('$course','$id','$code','$name','$credit','$time','$available','$description')";
     	if(mysqli_query($con,$q))
		echo "success";
	}
}
?>
