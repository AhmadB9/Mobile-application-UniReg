<?php
require_once 'conx.php';
$id =$_GET['id'];
$time =$_GET['time'];
$courseName=$_GET['course'];
$instructor=$_GET['instructor'];
$semester=$_GET['semester'];
$que="select maxcredit from student where id='$id' and semester='$semester'";
$r = mysqli_query($con,$que);
if(mysqli_num_rows($r)>0){
	while ($row = mysqli_fetch_assoc($r)) {
		$maxcredit=$row['maxcredit'];
}
}
$query="select courseid,credit from courses where coursename='$courseName'and instructor='$instructor' and time='$time'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
	while ($row = mysqli_fetch_assoc($result)) {
		$courseID=$row['courseid'];
		$credit=$row['credit'];}
	$q ="Delete from registeredcourse where courseid='$courseID' and studentid='$id'";
    if(mysqli_query($con,$q)){
		$newmaxcredit=$maxcredit+$credit;
		$qu="UPDATE student SET maxcredit='$newmaxcredit' WHERE id='$id' and semester='$semester'";
		if(mysqli_query($con,$qu))
			echo "success";
		}
}
?>