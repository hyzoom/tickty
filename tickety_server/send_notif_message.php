<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<?php
$error='';

require_once('./db_functions.php'); 
require_once ('./config.php'); 
include_once './GCM.php';
$gcm = new GCM();

global $con;
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);

if (isset($_POST['send'])) {
if (empty($_POST['noti_content'])) {
$error = "Invalid";
}
else{//if (isset($_POST["regId"]) && isset($_POST["message"])){
  $message = $_POST["noti_content"];
  
$result1 = mysqli_query($con,"SELECT * FROM users") or die(mysqli_error($con));

if (mysqli_num_rows($result1) > 0) {
$message = array("go" => $message);
while ($row = mysqli_fetch_array($result1)) {

    $regId = $row["gcm_regid"];

    
$registatoin_ids = array($regId);

$result = $gcm->send_notification($registatoin_ids, $message);
 
    echo $result;
}

    

    
}
    
}
}
?>
