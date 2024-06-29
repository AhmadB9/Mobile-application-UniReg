<?php
require_once 'conx.php';
$query="select id from users where type='student'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['id'] = $row['id'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>