package com.example.projet_e5_version_final.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_e5_version_final.R;
import com.example.projet_e5_version_final.RDVActivity;
import com.example.projet_e5_version_final.RdvDetaileActivity;
import com.example.projet_e5_version_final.services.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdapterTest extends BaseAdapter {
    private Context context;
    private JSONArray jsonarray;
    private LayoutInflater inflater;

    public AdapterTest(Context context,JSONArray jsonArray){
        this.context = context;
        this.jsonarray = jsonArray;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return jsonarray.length();
    }

    @Override
    public Object getItem(int i) {
        return jsonarray.optJSONObject(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.list_test,viewGroup,false);
        }

        TextView tv = view.findViewById(R.id.tv_nom_test);
        JSONObject jsonObject = (JSONObject) getItem(i);
        tv.setText(jsonObject.optString("id"));

        return view;
    }
}
