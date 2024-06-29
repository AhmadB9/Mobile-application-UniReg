<?php
require_once 'conx.php';
$course=$_GET['course'];
$id =$_GET['id'];
$query = "select registeredcourse.grade from courses 
JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
where  coursename='$course' and studentid='$id'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	echo $row['grade'];	
}}

?>