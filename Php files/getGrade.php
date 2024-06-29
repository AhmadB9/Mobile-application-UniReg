<?php
require_once 'conx.php';
$id =$_GET['id'];
$courseName=$_GET['course'];
$query = "select grade from courses 
JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
where  studentid='$id'and coursename='$courseName'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	echo $row['grade'];	
}}
?>