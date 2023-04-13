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
