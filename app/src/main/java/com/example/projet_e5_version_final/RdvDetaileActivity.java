package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.projet_e5_version_final.services.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class RdvDetaileActivity extends AppCompatActivity {

    private String id_patient,email_doctor;
    TextView tv_detaile_np,tv_detaile_email,tv_detaile_address,tv_detaile_tele,tv_detaile_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv_detaile);

        Intent intent = getIntent();
        id_patient = intent.getStringExtra("id");
        email_doctor = intent.getStringExtra("email");

        tv_detaile_np = findViewById(R.id.tv_detaile_np);
        tv_detaile_email = findViewById(R.id.tv_detaile_email);
        tv_detaile_address = findViewById(R.id.tv_detaile_address);
        tv_detaile_tele = findViewById(R.id.tv_detaile_tele);
        tv_detaile_sp = findViewById(R.id.tv_detaile_sp);

        try {
            get_info_doctor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected void get_info_doctor() throws InterruptedException, JSONException {
        String values = "{email:" +
                email_doctor + "}";

        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("get_doctor", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();

        String res = api.get_Values();

        JSONObject obj = new JSONObject(res);
        tv_detaile_np.setText(obj.getString("first_name") + " " + obj.getString("last_name"));
        tv_detaile_email.setText(obj.getString("email"));
        tv_detaile_address.setText(obj.getString("office_address"));
        tv_detaile_tele.setText(obj.getString("phone_number"));

    }
}