package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class MonCompteActivity extends AppCompatActivity {

    public EditText ed_nom,ed_prenom,ed_address,ed_tele,ed_date;
    public Button button_change;
    public static MonCompteActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);
        MonCompteActivity.instance = this;

        ed_nom = findViewById(R.id.ed_mc_nom);
        ed_prenom = findViewById(R.id.ed_mc_prenom);
        ed_address = findViewById(R.id.ed_mc_address);
        ed_tele = findViewById(R.id.ed_mc_tele);
        ed_date = findViewById(R.id.ed_mc_date);

        try {
            get_user_info();
        } catch (InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }

    }


    protected void get_user_info() throws InterruptedException, JSONException {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        String values = "{id:" + id + "}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user_By_id", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);

        api.start();

        api.join();

        JSONObject json_obj = new JSONObject(api.get_Values());

        ed_nom.setText(json_obj.getString("first_name"));
        ed_prenom.setText(json_obj.getString("last_name"));
        ed_address.setText(json_obj.getString("address"));
        ed_tele.setText(json_obj.getString("phone_number"));
        ed_date.setText(json_obj.getString("birthdate"));

        button_change = findViewById(R.id.button_mc_change);
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                button_change.setText("SAVE");

                ed_nom.setEnabled(true);
                ed_prenom.setEnabled(true);
                ed_address.setEnabled(true);
                ed_tele.setEnabled(true);
                ed_date.setEnabled(true);

                String new_nom = String.valueOf(ed_nom.getText());
                String new_prenom = String.valueOf(ed_prenom.getText());
                String new_address = String.valueOf(ed_address.getText());
                String new_tele = String.valueOf(ed_tele.getText());
                String new_date = String.valueOf(ed_date.getText());

                String values = "{first_name:" + new_nom
                        + ",last_name:" + new_prenom
                        + ",address:" + new_address
                        + ",phone_number:" + new_tele
                        + ",birthdate:" + new_date
                        + ",type:" + type
                        + ",id:" + id
                        + "}";

                ArrayList<String> listValues = new ArrayList<>(Arrays.asList("update_user_info", "None", "Jiojio000608.", values));

                Api api = new Api(listValues);

                api.start();

                try {
                    api.join();
                    String res = api.get_Values();
                    JSONObject json_obj = new JSONObject(res);
                    Toast.makeText(MonCompteActivity.this,json_obj.getString("response"), Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }
}