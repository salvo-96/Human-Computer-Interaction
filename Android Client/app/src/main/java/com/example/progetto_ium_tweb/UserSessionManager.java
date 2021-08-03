package com.example.progetto_ium_tweb;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserSessionManager {

    SharedPreferences preference;

    Editor editor;

    Context _context;

    public static String ip = "172.20.10.14";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_SESSION_ID = "jsession_id";

    public UserSessionManager(Context context){

        this._context = context;

        preference = _context.getSharedPreferences("userSession", Context.MODE_PRIVATE);

        editor = preference.edit();

    }

    public void createUserLoginSession(String session){


        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_SESSION_ID, session);

        editor.commit();

    }


    public String getIp(){

        return this.ip;

    }

    public String getUserSession(){

        return preference.getString(KEY_SESSION_ID,null);

    }



    public void logoutUser(Context context){

        String url_logout = "http://" + this.ip + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + this.getUserSession() + "?servlet=session&submit=logout&android=true";

        downloadJSON(url_logout);

        editor.clear();

        editor.commit();


        Intent i = new Intent(context, Login.class);


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        context.startActivity(i);



    }


    private void downloadJSON(final String urlWebService){


        class DownloadJSON extends AsyncTask<Void,Void,String> {


            @Override
            protected void onPreExecute(){

                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s){


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

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null){


                        sb.append(json + "\n");


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


    private void loadIntoListView(String json) throws JSONException {





    }

}