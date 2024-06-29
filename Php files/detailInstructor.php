<?php
require_once 'conx.php';
$name =$_GET['name'];
$query = "select users.name,users.email,instructor.rate,instructor.votersnb,instructor.feedback,instructor.certificates,instructor.performance from users 
JOIN instructor ON users.id=instructor.id
where  users.name='$name'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['performance'] = $row['performance'];
	$row_array['name'] = $row['name'];
	$row_array['email'] = $row['email'];
	$row_array['feedback'] = $row['feedback'];
	$row_array['rate'] = $row['rate'];
	$row_array['voters'] = $row['votersnb'];
	$row_array['certificates'] = $row['certificates'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>