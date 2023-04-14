# Projet_android_job

## Comment utiliser les API (JAVA) :

### 1. Crée une chaîne au format JSON qui contient plusieurs paires de clé-valeur.

<pre>
      String values = "{email:" + email +
                                ",password:" + password +
                                ",first_name:" + nom +
                                ",last_name:" + prenom +
                                ",phone_number:" + tele +
                                ",birthdate:" + anniversaire +
                                ",address:" + address +
                                ",genre:" + genre +
                                ",type:" + "patient"
                                + "}";
</pre>

### 2. ArrayList est créée en utilisant un constructeur qui prend un tableau de valeurs comme argument. 

<pre>
      ArrayList<String> listValues = new ArrayList<>(Arrays.asList("inscription", "None", "Jiojio000608.", values));
</pre>

### 3. Un objet de la classe "Api" est créé en utilisant le constructeur qui prend une instance d'ArrayList comme argument.

<pre>
      Api api = new Api(listValues);
</pre>

### 4.  La méthode "start()" est appelée sur cet objet "api". Cette méthode démarre l'exécution du thread associé à l'objet "api", qui exécutera le code défini dans la méthode "run()" de la classe "Api".

<pre>
      api.start();
</pre>

### 5. La méthode "join()" est utilisée pour attendre que l'exécution du thread associé à l'objet "api" soit terminée avant de continuer l'exécution du programme principal. 

<pre>
      api.join();
</pre>

### 6.Obtenir le résultat renvoyé par l'API (type chaîne).

<pre>
      api.get_Values();
</pre>

### 7.En fonction des données renvoyées par l'API, déterminez si vous souhaitez les convertir au format JSONObject ou au format JSONArray. Voici deux exemples simples ci-dessous :

<pre> 
      // JSONObject 
      JSONObject res_obj = new JSONObject(api.get_Values());
    
      //JSONArray
      JSONArray jsonArray = new JSONArray(api.get_Values());
</pre>

##### [Cliquez ici pour voir la documentation officielle de "org.json"](https://stleary.github.io/JSON-java/index.html)

# API Explication

### 1.find_user_By_email

<pre>
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
</pre>

#### PS: Email = Send_email

<pre>

// send email = houzeyu7@gmail.com

{
    "id": "11",
    "first_name": "Zeyu",
    "last_name": "",
    "birthdate": "1998-05-12",
    "gender": "M",
    "phone_number": "0695867276",
    "email": "houzeyu7@gmail.com",
    "address": "33 rue louise 75013 paris ",
    "password": "$2y$10$d/J43sCEeHej3R66mN7Z/ORVD0fdeZoBWIhEgjKVopGGVZMJEUTCO",
    "type": "patient"
}


</pre>

