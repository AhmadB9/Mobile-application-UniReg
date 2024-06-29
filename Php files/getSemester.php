<?php
require_once 'conx.php';
$course=$_GET['course'];
$query="select available from courses where coursename='$course'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['available'] = $row['available'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>