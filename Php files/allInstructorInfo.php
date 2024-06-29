<?php
require_once 'conx.php';
$id =$_GET['id'];
$query="select * from users JOIN instructor ON users.id=instructor.id where users.id='$id'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['email'] = $row['email'];
	$row_array['pass'] = $row['password'];
	$row_array['name'] = $row['name'];
	$row_array['phone'] = $row['phone'];
	$row_array['certificate']=$row['certificates'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>