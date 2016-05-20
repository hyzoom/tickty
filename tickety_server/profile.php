

<?php


include('create_subject.php'); // Includes Login Script
require_once ('./config.php'); 
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);

$result = mysqli_query($con,"SELECT * FROM categories");


?>

<!DOCTYPE html>
<html>
<head>
<title>Enter new subject</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="profile">

<form action="" method="post">
<label>Ticket Number :</label>
<input id="title" name="title" placeholder="AS01283" type="text">

<label> Ticket Image Link :</label>
<input id="url" name="image_url" placeholder="Ticket Image" type="text">

<label>Note :</label>
<textarea id = "content" name="content" rows="20" cols="100"></textarea>


<label>From :</label>
<select name="categ_id">
<?php
$i=0;
while($row = mysqli_fetch_array($result)) {
?>
<option value="<?=$row["id"];?>"><?=$row["title"];?></option>
<?php
$i++;
}
?>
</select>

<label>To :</label>
<select name="categ_id">
<?php
$i=0;
while($row = mysqli_fetch_array($result)) {
?>
<option value="<?=$row["id"];?>"><?=$row["title"];?></option>
<?php
$i++;
}
?>
</select>



<input name="submit" type="submit" value=" SAVE ">
<span><?php echo $error; ?></span>

 
</form>

</div>
</body>
</html>
