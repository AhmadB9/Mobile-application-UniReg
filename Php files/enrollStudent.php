<?php
require_once 'conx.php';
$id =$_GET['id'];
$time =$_GET['time'];
$courseName=$_GET['course'];
$instrutor=$_GET['instructor'];
$semester=$_GET['semester'];
$query = "select * from courses 
JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
where coursename='$courseName' and studentid='$id'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
	echo "registered";}
else{
	$query1 = "select * from courses 
	JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
	where time='$time' and studentid='$id' and semester='$semester'";
	$res = mysqli_query($con,$query1);
	if(mysqli_num_rows($res)>0)echo "conflict";
	else {
		$query2="select courseid from courses where coursename='$courseName' and instructor='$instrutor' and time='$time'";
		$result2 = mysqli_query($con,$query2);
		if(mysqli_num_rows($result2)>0){
		while ($row = mysqli_fetch_assoc($result2)) {
			$courseID=$row['courseid'];
			$q="INSERT INTO registeredcourse(courseid,studentid,semester,grade) VALUES('$courseID','$id','$semester','0')";
			if(mysqli_query($con,$q))
				echo "success";
			}
		}
	}
}
?>
