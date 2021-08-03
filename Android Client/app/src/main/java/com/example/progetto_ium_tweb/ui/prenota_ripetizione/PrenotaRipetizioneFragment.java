package com.example.progetto_ium_tweb.ui.prenota_ripetizione;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.progetto_ium_tweb.R;
import com.example.progetto_ium_tweb.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PrenotaRipetizioneFragment extends Fragment {

    String url_get_corsi;

    Spinner spinner_corsi;

    Spinner spinner_prof_from_corso;

    Spinner spinner_giorno_from_prof;

    Button btn_prenota;

    private String corso_scelto;

    private String prof_scelto;

    private String giorno_scelto;

    private String ora_scelta;

    UserSessionManager userSessionManager;

    String json_id = "";

    final static String KEY_OPERAZIONE_GET_CORSI = "getAllCorsi";

    final static String KEY_OPERAZIONE_GET_PROF_FROM_CORSO = "profFromCorso";

    final static String KEY_OPERAZIONE_GET_DATA_FROM_PROF = "dataFromProf";

    final static String KEY_OPERAZIONE_GET_ORA_FROM_GIORNO = "oraFromGiorno";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_prenota_ripetizione, container, false);

        userSessionManager = new UserSessionManager(getContext());

        json_id = userSessionManager.getUserSession();

        url_get_corsi = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?servlet=index&operazione=get_all_corsi";

        downloadJSON(url_get_corsi,KEY_OPERAZIONE_GET_CORSI);

        return root;

    }




    private void downloadJSON(final String urlWebService, final String key_operazione){


        class DownloadJSON extends AsyncTask<Void,Void,String> {


            @Override
            protected void onPreExecute(){

                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s){


                super.onPostExecute(s);


                switch (key_operazione) {


                    case "getAllCorsi":

                        try {

                            loadIntoListView(s);

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                        break;

                    case "profFromCorso":

                        try {

                            loadIntoListView_prof(s);

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                        break;

                    case "dataFromProf":

                        try {

                            loadIntoListView_ripetizioni_from_giorno(s);

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                        break;

                    case "oraFromGiorno":

                        try {

                            loadIntoListView_prenota_ripetizione(s);

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                        break;


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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void loadIntoListView(String json) throws JSONException {


        ArrayList<String> corsi = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(json);

        corsi.add(" ");

        for(int i=0;i<jsonArray.length();i++) {


            JSONObject line = jsonArray.getJSONObject(i);

            String lineName = line.optString("titolo");

            corsi.add(lineName);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, corsi);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_corsi = getView().findViewById(R.id.spinner_corsi);

        spinner_corsi.setAdapter(adapter);

        spinner_corsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String corso = parent.getItemAtPosition(position).toString();

                corso_scelto = corso;

                String url_get_prof_from_corso = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?servlet=index&"
                        +"operazione=get_professore_from_corso&titolo_corso="+corso;

                downloadJSON(url_get_prof_from_corso,KEY_OPERAZIONE_GET_PROF_FROM_CORSO);

            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

    }


    private void loadIntoListView_prof(String json) throws JSONException {


        ArrayList<String> prof = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(json);


        for(int i=0;i<jsonArray.length();i++) {


            JSONObject line = jsonArray.getJSONObject(i);

            String id = line.optString("id");

            prof_scelto = id;

            String lineName = line.optString("nome") + " " + line.optString("cognome");


            prof.add(lineName);


        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, prof);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_prof_from_corso = getView().findViewById(R.id.spinner_prof_from_corso);

        spinner_prof_from_corso.setAdapter(adapter);



        // RECUPERO IL GIORNO

        spinner_giorno_from_prof = getView().findViewById(R.id.spinner_giorno_from_prof);

        spinner_giorno_from_prof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String giorno = parent.getItemAtPosition(position).toString();

                giorno_scelto = giorno;

                String url_get_ripetizione_from_giorno = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?servlet=index&"
                        +"operazione=get_ripetizioni_from_giorno&giorno="+giorno;

                downloadJSON(url_get_ripetizione_from_giorno,KEY_OPERAZIONE_GET_DATA_FROM_PROF);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void loadIntoListView_ripetizioni_from_giorno(final String json) throws JSONException {

        ArrayList<String> ripetizioni_disponibili = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(json);

        for(int i=0;i<jsonArray.length();i++) {

            JSONObject line = jsonArray.getJSONObject(i);


            String lineName = line.optString("data") + line.optString("ora") + line.optString("id_professore") + line.optString("titolo_corso") + line.optString("mail_utente");


            ripetizioni_disponibili.add(lineName);

        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ripetizioni_disponibili);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        Spinner spinner_ora = (Spinner) getView().findViewById(R.id.spinner_ora);

        spinner_ora.setAdapter(adapter);



        spinner_ora.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String ora = parent.getItemAtPosition(position).toString();

                ora_scelta = ora;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_prenota = getView().findViewById(R.id.button_prenota);

        btn_prenota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url_prenota = "http://"+ userSessionManager.getIp() +":8080/Progetto-IUM-TWEB/servlet_server;jsessionid="+json_id+"?servlet=user&operazione=prenota&titolo_corso="+corso_scelto+"&id_professore="+
                        prof_scelto+"&data="+giorno_scelto+"&ora="+ora_scelta;

                downloadJSON(url_prenota,KEY_OPERAZIONE_GET_ORA_FROM_GIORNO);
            }
        });

    }

    private void loadIntoListView_prenota_ripetizione(String json) throws JSONException {

        if(json.equals("true"))
            Toast.makeText(getActivity(),"Ripetizione Prenotata",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),"Impossibile prenotare ripetizione",Toast.LENGTH_SHORT).show();

    }


}