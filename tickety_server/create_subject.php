<?php
$response = array();
require_once('./db_functions.php'); 
require_once ('./config.php'); 
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);

$db = new DB_Functions();

$error=''; // Variable To Store Error Message
if (isset($_POST['submit'])) {

// Define $username and $password
if (empty($_POST['title']) || empty($_POST['content'])|| empty($_POST['image_url'])) {
$error = "Invalid";
}
else{//if (isset($_POST['title']) && isset($_POST['content'])&& isset($_POST['image_url'])) {//
 
    $title=$_POST['title'];
    $content=$_POST['content'];
    $image_url=$_POST['image_url'];
    $categ_id=$_POST['categ_id'];

    // mysql inserting a new row
    $result = mysqli_query($con,"INSERT INTO subjects(title, content, image_url,categ_id, created_at) VALUES('$title', '$content', '$image_url','$categ_id', NOW())");
 
    // check if row inserted or not
    if ($result) {
        $error = "Subject successfully created.";
    } else {
	 $error = "Oops! An error occurred.";
    }

} 

}

?>
