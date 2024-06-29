<?php
require_once 'conx.php';
$course=$_GET['course'];
$id =$_GET['id'];
$grade=$_GET['grade'];
$query = "UPDATE courses INNER JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
SET registeredcourse.grade='$grade'
where  courses.coursename='$course' and registeredcourse.studentid='$id'";
if(mysqli_query($con, $query))echo "success";
?>