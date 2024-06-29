<?php
require_once 'conx.php';
$id=$_GET['id'];
$course=$_GET['course'];
$code=$_GET['code'];
$time=$_GET['time'];
$description=$_GET['description'];
$credit=$_GET['credit'];
$available=$_GET['available'];
$query="select courseid from courses where coursename='$course' and instructorID='$id' and time='$time'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
    while ($row = mysqli_fetch_assoc($result)) { 
		$courseID=$row['courseid'];
		$q="UPDATE courses SET coursename='$course',code='$code',credit='$credit',time='$time',description='$description' WHERE courseid='$courseID'";
		if(mysqli_query($con, $q))echo "success";
		}
	}

?>