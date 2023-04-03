<?php

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

class api{

    private $conn,$table_name,$token,$values;
    public $obj_url;

    public function __construct(){
        require_once "./config/database.php";
        require_once "./bin/AES.php";
        require_once "./bin/check.php";

        $database = new database();
        $this->conn = $database->getConnection();
        $this->check = new check();
        $this->ASE = new AES();

        $status_connection = $database->getStatus();

        if ($status_connection === "true"){
            $this->get_Url();

            if ($this->obj_url->token === "Jiojio000608."){
                $this->call_function($this->obj_url->function_name,[]);
            }else{
                echo "{status:false}";
            }
        }else{
            echo "{status:false}";
        }
    }



    //query contient
    public function get_Url(){
        $url = $_SERVER["HTTP_HOST"] . $_SERVER["REQUEST_URI"];
        $url_parse = parse_url($url);
        $url_query = $this->convertUrlArray($url_parse["query"]);
        $list_parse = array();

        foreach ($url_query as $list){
            array_push($list_parse,$this->ASE->decrypt($list));
        }

        $objArray = new ObjArray($list_parse[0], $list_parse[1], $list_parse[2],$list_parse[3]);

        $valuesArray = array();
        $splitValues = explode(',', substr($list_parse[3], 1, -1));
        foreach ($splitValues as $value) {
            list($key, $val) = explode(':', $value);
            $valuesArray[$key] = $val;
        }

        $objArray->values = $valuesArray;

        $this->obj_url = $objArray;

    }

    public function convertUrlArray($query)
    {
        $queryParts = explode('&', $query);
        $params = array();
        foreach ($queryParts as $param) {
            $item = explode('=', $param);
            $params[$item[0]] = $item[1];
        }
        return $params;
    }

    public function call_function($name_function,$options){
        return call_user_func_array(array($this,$name_function),$options);
    }

    // {email:houzeyu7@gmail.com}
    public function login(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_partients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_partients->bindParam(':email',$this->obj_url->values["email"]);
        $stmt_doctors->bindParam(':email',$this->obj_url->values["email"]);

        $stmt_partients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_partients->fetch(PDO::FETCH_ASSOC);
        $res_doctors = $stmt_doctors->fetch(PDO::FETCH_ASSOC);

        if (count($res_patients) > 0){
            $res_patients["type"] = "patient";
            echo json_encode($res_patients);
        }else{
            $res_doctors["type"] = "docteur";
            echo json_encode($res_doctors);
        }
    }

    // {key:d} Utilisé pour rechercher des médecins Compatible avec la recherche floue
    public function find_doctor() {

        $sql = "SELECT * FROM doctors WHERE first_name LIKE :key1 OR last_name LIKE :key2";
        $stmt = $this->conn->prepare($sql);
        $search_keyword = '%' . $this->obj_url->values["key"] . '%';

        $stmt->bindParam(':key1', $search_keyword);
        $stmt->bindParam(':key2', $search_keyword);

        $stmt->execute();
        $res_all_doctors = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res_all_doctors);
    }

    //{doctor_id:1} Médecin pour rechercher tous les rendez-vous en attente
    public function find_patient_Rdv(){
        $sql = "SELECT * FROM appointments WHERE doctor_availability_id IN 
                                 (SELECT id FROM doctor_availabilities WHERE doctor_id = :id)";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(':id',$this->obj_url->values["doctor_id"]);

        $stmt->execute();

        $res_all_patients = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res_all_patients);
    }

    //{email:houzeyu7@gmail.com}
    public function find_user_By_email(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_patients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patients->bindParam(':email',$this->obj_url->values["email"]);
        $stmt_doctors->bindParam(':email',$this->obj_url->values["email"]);

        $stmt_patients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_patients->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);

        if (count($res_patients) > 0){
            $res_patients["type"] = "patient";
            echo json_encode($res_patients);
        }else{
            $res_doctor["type"] = "doctor";
            echo json_encode($res_doctor);
        }
    }

    //{email:houzeyu7@gmail.com,type:patient,password:Jiojio000104.}
    public function change_password(){
        $type = $this->obj_url->values["type"];

        if ($type === "patient"){
            $sql = "UPDATE patients SET password = :password WHERE email = :email";
            $stmt = $this->conn->prepare($sql);

            $stmt->bindParam("password",$this->obj_url->values["password"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $stmt->execute();

            echo json_encode("true patient");
        }else{
            $sql = "UPDATE doctors SET password = :password WHERE email = :email";
            $stmt = $this->conn->prepare($sql);

            $stmt->bindParam("password",$this->obj_url->values["password"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $stmt->execute();

            echo json_encode("true doctor");
        }
    }

    //{email:houzeyu7@gmail.com}
    public function check_user_double(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_patient = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patient->bindParam("email",$this->obj_url->values["email"]);
        $stmt_doctors->bindParam("email",$this->obj_url->values["email"]);

        $stmt_patient->execute();
        $stmt_doctors->execute();

        $res_patient = $stmt_patient->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);



        if ($res_patient !== false || $res_doctor !== false){
            if ($res_patient !== false){
                $message = [
                    "type" => "patient",
                    "user_existe" => "true"
                ];
                echo json_encode($message);
            }else{
                $message = [
                    "type" => "doctor",
                    "user_existe" => "true"
                ];
                echo json_encode($message);
            }
        }else{
            $message = [
                "type" => "null",
                "user_existe" => "false"
            ];
            echo json_encode($message);
        }
    }

    //{type:doctor,email:sophiabrown@example.com,phone_number:000122333,office_address:3333333}
    //{type:patient,email:sophiabrown@example.com,first_name:hou,last_name:zeyu,birthdate:1997-05-12,,phone_number:000122333,address:3333333}
    public function update_user_info(){
        if ($this->obj_url->values["type"] === "patient"){
            $sql = "UPDATE patients SET first_name = :first_name,last_name = :last_name,birthdate = :birthdate,phone_number = :phone_number,address = :address WHERE email = :email";
            $stmt = $this->conn->prepare($sql);
            $stmt->bindParam("first_name",$this->obj_url->values["first_name"]);
            $stmt->bindParam("last_name",$this->obj_url->values["last_name"]);
            $stmt->bindParam("birthdate",$this->obj_url->values["birthdate"]);
            $stmt->bindParam("phone_number",$this->obj_url->values["phone_number"]);
            $stmt->bindParam("address",$this->obj_url->values["address"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $res = $stmt->execute();

            $message = [
                "response" => $res
            ];

            echo json_encode($message);
        }else{
            $sql = "UPDATE doctors SET phone_number = :phone_number,office_address = :office_address WHERE email = :email";
            $stmt = $this->conn->prepare($sql);
            $stmt->bindParam("phone_number",$this->obj_url->values["phone_number"]);
            $stmt->bindParam("office_address",$this->obj_url->values["office_address"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $res = $stmt->execute();

            $message = [
                "response" => $res
            ];

            echo json_encode($message);
        }
    }
}


class ObjArray {
    public $function_name;
    public $tablename;
    public $token;
    public $values;

    public function __construct($function_name,$tablename, $token, $values) {
        $this->function_name = $function_name;
        $this->tablename = $tablename;
        $this->token = $token;
        $this->values = $values;
    }
}

$api = new api();
