<?php
require_once 'conx.php';
$id=$_GET['id'];
$semester=$_GET['semester'];
$credit=0;
$query = "select courses.coursename,courses.instructor,courses.credit,courses.time from courses 
JOIN registeredcourse ON registeredcourse.courseid = courses.courseid
where  studentid='$id'and semester='$semester'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
$credit+= $row['credit'];}
	$row_array['credit']=$credit;
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);