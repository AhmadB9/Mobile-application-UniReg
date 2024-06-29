<?php
require_once 'conx.php';
$name=$_GET['name'];
$query="select * from courses where coursename='$name'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['course'] = $row['coursename'];
	$row_array['instructor'] = $row['instructor'];
	$row_array['time'] = $row['time'];
	$row_array['credit'] = $row['credit'];
	$row_array['available'] = $row['available'];
	$row_array['description'] = $row['description'];
array_push($return_array['list'], $row_array);}
}echo json_encode($return_array);
?>