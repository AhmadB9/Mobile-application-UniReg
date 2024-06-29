<?php
require_once 'conx.php';
$id =$_GET['id'];
$semester =$_GET['semester'];
$courseName=$_GET['course'];
$query = "select  * from courses
JOIN requiredcourses ON requiredcourses.courseid = courses.courseid
where  studentid='$id' and courses.coursename='$courseName'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
	echo"Already Enrolled";
}
else{
$query="select courseid from courses where coursename='$courseName'";
$result = mysqli_query($con,$query);
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$courseID=$row['courseid'];
	$q ="INSERT INTO requiredcourses(courseid,studentid,semester) VALUES('$courseID','$id','$semester')";
    if(mysqli_query($con,$q))echo "";
}}}

?>