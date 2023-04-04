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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.projet_e5_version_final.services.Api;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private MenuItem menu_moncompte,menu_log_out;
    private String id,type;
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
                System.out.println(item.getTitle());
                if(item.getTitle().equals("SE CONNECTER ")){
                    Intent intent_login = new Intent();
                    intent_login.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent_login);
                }

                if (item.getTitle().equals("Log Out")){


                    NavigationView nv = findViewById(R.id.nav_view);
                    nv.getMenu().clear(); // 清除原有菜单
                    nv.inflateMenu(R.menu.nav_menu); // 加载新菜单

                    Intent intent = getIntent();
                    intent.replaceExtras(new Bundle());
                    finish();
                    startActivity(intent);


                    TextView tv_titie1 = findViewById(R.id.main_title1);
                    TextView tv_title2 = findViewById(R.id.main_title2);

                    tv_titie1.setText("Trouvez un rendez-vous avec");
                    tv_title2.setText("une médecin généraliste");
                }
                return true;
            }
        });

        Intent info_login = getIntent();
        id = info_login.getStringExtra("id");
        type = info_login.getStringExtra("type");
        System.out.println(id + type);
        if (id != null && type != null){
            String values = "{id:" + id + "}";
            ArrayList<String> listValues = new ArrayList<>(Arrays.asList("find_user_By_id", "None", "Jiojio000608.", values));
            Api api = new Api(listValues);
            api.start();
            change_menu_user(navigationView);
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

    protected void change_menu_user(NavigationView navigationview){
        Menu menu = navigationview.getMenu();
        menu_moncompte = menu.add("Mon Compte");
        menu_log_out = menu.add("Log Out");
        menu.removeItem(R.id.menu_se_connecter);
        menu.removeItem(R.id.menu_mon_compte);
        menu.removeItem(R.id.menu_inscription);
    }



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
