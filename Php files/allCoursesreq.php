<?php
require_once 'conx.php';
$id=$_GET['id'];
$query = "select courses.coursename from courses 
JOIN requiredcourses ON requiredcourses.courseid = courses.courseid
where  studentid='$id'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['course'] = $row['coursename'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>