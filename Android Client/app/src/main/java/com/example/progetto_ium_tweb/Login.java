package com.example.progetto_ium_tweb;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Login extends AppCompatActivity {

    private String json_id = "";

    private EditText email, password;
    private Button login;
    private Button register;
    private CheckBox checkBox;
    private TextView message;

    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.text_mail);
        password = findViewById(R.id.text_password);
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);
        checkBox = findViewById(R.id.checkBox);
        message = findViewById(R.id.message);
        message.setVisibility(View.INVISIBLE);

        userSessionManager = new UserSessionManager(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches("") ||
                        password.getText().toString().matches("")) {
                    message.setText("Riempire tutti i campi.");
                    message.setVisibility(View.VISIBLE);
                } else {
                    String url = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?" +
                            "servlet=session&" +
                            "submit=login&" +
                            "email=" + email.getText().toString() +
                            "&password=" + password.getText().toString()
                            +"&android=true";
                    downloadJSON(url);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    java.net.URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    json_id = bufferedReader.readLine();
                    userSessionManager.createUserLoginSession(json_id);
                    StringBuilder sb = new StringBuilder();
                    String json_user;
                    while ((json_user = bufferedReader.readLine()) != null){
                        sb.append(json_user + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }



        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }


    private void loadIntoListView(String json_id) throws JSONException {
        if (json_id.equals("{}")) {
            message.setText("Credenziali errate.");
            message.setVisibility(View.VISIBLE);
        } else {
            JSONObject utente = new JSONObject(json_id);
            if (utente.getString("mail").equals(email.getText().toString()) &&
                    utente.getString("password").equals(password.getText().toString()) &&
                    utente.getString("ruolo").equals("user")) {
                Toast.makeText(getApplicationContext(), "Benvenuto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("nome", utente.getString("nome"));
                intent.putExtra("cognome", utente.getString("cognome"));
                startActivity(intent);
            } else {
                message.setText("Accesso negato.");
                message.setVisibility(View.VISIBLE);
            }
        }
    }

}
