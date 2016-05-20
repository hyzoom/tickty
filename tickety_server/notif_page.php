
<?php

echo '<body dir="rtl">';
include('send_notif_message.php'); // Includes send Script

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

<label>—”«·… «·‰ Ê›Ìﬂ«‘‰:</label>
<textarea id = "noti_content" name="noti_content" rows="20" cols="100"></textarea>



<input name="send" type="submit" value=" SEND ">
<span><?php echo $error; ?></span>
</form>

</div>
</body>
</html>

