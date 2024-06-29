<?php
require_once 'conx.php';
$comment=$_GET['comment'];
$instructor =$_GET['instructor'];
$rate =$_GET['rate'];
$per=$_GET['performance'];
$newfeedback='';
$query = "Select instructor.rate,instructor.votersnb,instructor.feedback,instructor.performance from users 
JOIN instructor ON users.id=instructor.id
where  users.name='$instructor'";
$result = mysqli_query($con,$query);
if(mysqli_query($con, $query)){
	if(mysqli_num_rows($result)>0){
	while ($row = mysqli_fetch_assoc($result)) {
		$oldrate=floatval($row['rate']);
		$voters=$row['votersnb'];
		$feedback=$row['feedback'];
		$performance=$row['performance'];
}
}
if($oldrate==0){
	$newrate=floatval($rate);
	$voters=$voters + 1;
}
else {
	$newrate=floatval(($rate+$oldrate*$voters)/($voters+1));
	$voters=$voters + 1;
}
if($comment!='null'){
	if($feedback=='null')
		$newfeedback=$comment;
	else $newfeedback=$comment.":".$feedback;
}
if($performance=='null')
	$newperformance=$per;
else $newperformance=$per.":".$performance;
if($newfeedback!=''){
	$q = "UPDATE users INNER JOIN instructor ON users.id = instructor.id
	SET instructor.rate='$newrate',instructor.votersnb='$voters',feedback='$newfeedback',instructor.performance='$newperformance'
	where  users.name='$instructor'";
	if(mysqli_query($con, $q))echo "success";
}
else{
	$q = "UPDATE users INNER JOIN instructor ON users.id = instructor.id
SET instructor.rate='$newrate',instructor.votersnb='$voters',instructor.performance='$newperformance'
where  users.name='$instructor'";
if(mysqli_query($con, $q))echo "success";
}
}
?>