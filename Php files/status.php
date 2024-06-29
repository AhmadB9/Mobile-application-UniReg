<?php
require_once 'conx.php';
$id = $_GET['ID'];
$semester =$_GET['semester'];
$query = "select maxcredit,status from student  where ID='$id' and semester='$semester'";
$result = mysqli_query($con,$query);
$return_array = array();
$return_array['list'] = array();
if(mysqli_num_rows($result)>0){
while ($row = mysqli_fetch_assoc($result)) {
	$row_array['MaxCredit'] = $row['maxcredit'];
	$row_array['Status'] = $row['status'];
	array_push($return_array['list'], $row_array);
}
echo json_encode($return_array);
}
?>