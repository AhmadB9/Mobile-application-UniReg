<?php
require_once 'conx.php';
$id=$_GET['id'];
$semester=$_GET['semester'];
$query = "select courses.coursename,courses.instructor,courses.credit,courses.time from courses 
JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
where  studentid='$id'and semester='$semester'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['course'] = $row['coursename'];
	$row_array['instructor'] = $row['instructor'];
	$row_array['time'] = $row['time'];
	$row_array['credit'] = $row['credit'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>