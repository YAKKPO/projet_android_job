package com.example.projet_e5_version_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.projet_e5_version_final.services.Api;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int halfScreenWidth = screenWidth / 2;

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ViewGroup.LayoutParams layoutParams = navigationView.getLayoutParams();
        layoutParams.width = halfScreenWidth;
        navigationView.setLayoutParams(layoutParams);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                if(item.getTitle().equals("Login")){
                    Intent intent_login = new Intent();
                    intent_login.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent_login);
                }
                return true;
            }
        });

        Intent info_login = getIntent();
        String id = info_login.getStringExtra("id");
        String type = info_login.getStringExtra("type");
        if (id != null && type != null){
            String values = "{id:" + id + "}";
            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user_By_id", "None", "Jiojio000608.", values));
            Api api = new Api(listValues);
            api.start();

            try {
                api.join();
                JSONObject api_json = new JSONObject(api.get_Values());

                TextView tv_titie1 = findViewById(R.id.main_title1);
                TextView tv_title2 = findViewById(R.id.main_title2);

                tv_titie1.setText("Bonjour !");
                tv_title2.setText(api_json.getString("first_name") + " " + api_json.getString("last_name"));

            } catch (InterruptedException | JSONException e) {
                throw new RuntimeException(e);
            }
        }


    }

    //testfezef

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
