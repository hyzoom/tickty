<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
require_once('./db_functions.php'); 

require_once ('./config.php'); 
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);



$db = new DB_Functions();
 
	
	$result = mysqli_query($con,"SELECT * FROM categories") or die(mysql_error());
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["subjects"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["id"] = $row["id"];
        $product["title"] = $row["title"];
        $product["created_at"] = $row["created_at"];
  
 
        // push single product into final response array
        array_push($response["subjects"], $product);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response, JSON_UNESCAPED_UNICODE);

}
?>
