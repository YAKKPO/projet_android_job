package com.example.projet_e5_version_final.services;

import android.os.Build;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Api extends Thread{

    private String url_api,url_server = "https://www.zeyu.site/api/api.php";

    private String Token = "S8jlgW4doHq1Lh9mfqSu6Q";

    private String Key_AES = "bUYJ3nTV6VBasdJF";
    private ArrayList values;

    public Api(ArrayList<String> listValues) {
        this.values = listValues;
    }


    public void run() {
        set_Url_Api(this.values);
        System.out.println(this.url_api);
    }

    protected void set_Url_Api(ArrayList Values){
        String Key_AES = "bUYJ3nTV6VBasdJF";
        this.url_api = url_server + "?" + "jx7iCyX9c7wthGiqmlrWrQ=" + encrypt(Values.get(0).toString(),Key_AES) +
                "&exwX6tEWLJzXl6j9S7ZLaA=" + encrypt(Values.get(1).toString(),Key_AES) +
                "&wqLlE//YIwi2fOkmQa/6Eg=" + encrypt(Values.get(2).toString(),Key_AES) +
                "&BPDlAkeq+/jKaJUX2Ez1rg=" + encrypt(Values.get(3).toString(),Key_AES)
        ;
    }

    protected static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Base64.Encoder encoder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encoder = Base64.getEncoder();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(encoder.encodeToString(crypted));
        }else{
            return "null";
        }
    }

    protected static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            Base64.Decoder decoder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decoder = Base64.getDecoder();
            }
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                output = cipher.doFinal(decoder.decode(input));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }
}
