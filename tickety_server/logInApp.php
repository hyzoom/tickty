<?php
 
// response json
$json = array();
 require_once('./db_functions.php'); 

require_once ('./config.php'); 
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);


/**
 * Registering a user device
 * Store reg id in users table
 */

if (isset($_GET["name"]) && isset($_GET["password"])){
//(isset($_POST["name"]) && isset($_POST["password"])) {
    $name = $_GET["name"];
    $password = $_GET["password"];

	$result = mysqli_query($con,"SELECT *  FROM users WHERE name = '$name' and 	 password = '$password'") or die(mysql_error());

if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
//    $response["subjects"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["name"] = $row["name"];
     
       
  
 
        // push single product into final response array
        //array_push($response["subjects"], $product);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response, JSON_UNESCAPED_UNICODE);
 
} else {
    $response["success"] = 0;
    echo json_encode($response, JSON_UNESCAPED_UNICODE);
    // user details missing
}

}
?>
