package com.example.projet_e5_version_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.projet_e5_version_final.adapters.AdapterTest;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            test();
        } catch (InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //find_user_By_email
    protected void test() throws InterruptedException, JSONException {
        String values = "{null:null}";
        ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user", "None", "Jiojio000608.", values));
        Api api = new Api(listValues);
        api.start();

        api.join();
        String api_string = api.get_Values();

        JSONArray jsonArray = new JSONArray(api_string);

        ListView listView_test = findViewById(R.id.listView_test);
        BaseAdapter baseAdapter = new AdapterTest(this,jsonArray);
        listView_test.setAdapter(baseAdapter);

    }

}