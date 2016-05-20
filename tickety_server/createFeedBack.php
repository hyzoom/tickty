<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
require_once('./db_functions.php'); 
$db = new DB_Functions(); 
require_once ('./config.php'); 
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);

// check for required fields
if (isset($_GET['userId']) && isset($_GET['subjectId'])&& isset($_GET['comment'])) {
 
    $userId = $_GET['userId'];
    $subjectId = $_GET['subjectId'];
    $comment = $_GET['comment'];
    
 
 $result = mysqli_query($con,"INSERT INTO feedback(userId, subjectId, comment) VALUES('$userId', '$subjectId', '$comment')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "feedback successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>
