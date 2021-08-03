package com.example.progetto_ium_tweb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AppCompatActivity {

    private TextView message;
    private Button register;
    private EditText email, password, nome, cognome;
    private CheckBox checkBox;

    UserSessionManager userSessionManager;

    private String json_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.button_register);
        email = findViewById(R.id.text_mail);
        password = findViewById(R.id.text_password);
        nome = findViewById(R.id.text_nome);
        cognome = findViewById(R.id.text_cognome);
        checkBox = findViewById(R.id.checkBox);
        message = findViewById(R.id.message);
        message.setVisibility(View.INVISIBLE);

        userSessionManager = new UserSessionManager(getApplicationContext());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches("") ||
                        password.getText().toString().matches("") ||
                        nome.getText().toString().matches("") ||
                        cognome.getText().toString().matches("")) {
                    message.setText("Riempire tutti i campi");
                    message.setVisibility(View.VISIBLE);
                } else {
                    String url = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?" +
                            "servlet=session&" +
                            "submit=register&" +
                            "ruolo=user&" +
                            "email=" + email.getText().toString() +
                            "&password=" + password.getText().toString() +
                            "&nome=" + nome.getText().toString() +
                            "&cognome=" + cognome.getText().toString() +
                            "&android=true";

                    downloadJSON(url);
                }
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
            protected String doInBackground(Void... voids) {
                try {
                    java.net.URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    json_id = bufferedReader.readLine();
                    userSessionManager.createUserLoginSession(json_id);
                    StringBuilder sb = new StringBuilder();
                    String json_user;
                    while ((json_user = bufferedReader.readLine()) != null) {
                        sb.append(json_user + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

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
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {

        if (!json.equals("")) {
            Toast.makeText(this, "Benvenuto", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("nome", nome.getText().toString());
            intent.putExtra("cognome", cognome.getText().toString());
            startActivity(intent);
        } else {
            message.setText("Mail già registrata.");
            message.setVisibility(View.VISIBLE);
        }
    }
}
