
<?php
 require_once ('./config.php'); 

class DB_Functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }

public function storeUser($name, $email, $gcm_regid) {
        // insert user into database
$con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);
        $result = mysqli_query($con,"INSERT INTO users(name, email, gcm_regid, created_at) VALUES('$name', '$email', '$gcm_regid', NOW())");
        // check for successful store
        if ($result) {
            // get user details
            $id = mysqli_insert_id($con); // last inserted id
            $result = mysqli_query($con,"SELECT * FROM users WHERE id = $id") or die(mysqli_error());
            // return user details
            if (mysqli_num_rows($result) > 0) {
                return mysqli_fetch_array($result);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
 
    /**
     * Getting all users
     */
    public function getAllUsers() {
        $result = mysqli_query("select * FROM gcm_users");
        return $result;
    }


}
 
?>
