﻿<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
require_once('./db_functions.php'); 
$db = new DB_Functions();

// check for required fields
if (isset($_GET['name']) && isset($_GET['email'])) {
 
    $name = $_GET['name'];
    $email = $_GET['email'];
    
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO users(name, email, created_at) VALUES('$name', '$email', NOW())");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "user successfully created.";
 
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
