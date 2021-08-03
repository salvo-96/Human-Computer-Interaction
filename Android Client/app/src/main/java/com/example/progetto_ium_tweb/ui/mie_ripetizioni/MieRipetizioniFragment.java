package com.example.progetto_ium_tweb.ui.mie_ripetizioni;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progetto_ium_tweb.MyRecyclerViewAdapter;
import com.example.progetto_ium_tweb.MyRecyclerViewAdapter_ripetizioni_in_corso;
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


public class MieRipetizioniFragment extends Fragment implements MyRecyclerViewAdapter_ripetizioni_in_corso.ItemClickListener {

    MyRecyclerViewAdapter_ripetizioni_in_corso adapter_ripetizioni_in_corso;
    MyRecyclerViewAdapter adapter;
    UserSessionManager userSessionManager;
    String json_id = "";
    final static String KEY_OPERAZIONE_IN_CORSO = "inCorso";
    final static String KEY_OPERAZIONE_ANNULLATE = "annullate";
    final static String KEY_OPERAZIONE_COMPLETATE = "completate";
    static String url_ripetizioni_in_corso;
    static String url_ripetizioni_annullate;
    static String url_ripetizioni_completate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mie_ripetizioni, container, false);
        userSessionManager = new UserSessionManager(getContext());
        json_id = userSessionManager.getUserSession();
        Intent intent = new Intent(getContext(), MyRecyclerViewAdapter_ripetizioni_in_corso.class);
        intent.putExtra("json_id", json_id);
        url_ripetizioni_in_corso = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + json_id + "?servlet=user&operazione=get_ripetizioni_in_corso";
        url_ripetizioni_annullate = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + json_id + "?servlet=user&operazione=get_ripetizioni_annullate";
        url_ripetizioni_completate = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + json_id + "?servlet=user&operazione=get_ripetizioni_completate";
        downloadJSON(url_ripetizioni_in_corso, KEY_OPERAZIONE_IN_CORSO);
        downloadJSON(url_ripetizioni_annullate, KEY_OPERAZIONE_ANNULLATE);
        downloadJSON(url_ripetizioni_completate, KEY_OPERAZIONE_COMPLETATE);
        return root;
    }


    private void downloadJSON(final String urlWebService, final String key_operazione) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    switch (key_operazione) {
                        case "inCorso":
                            loadIntoListView_in_corso(s);
                            break;
                        case "annullate":
                            loadIntoListView_annullate(s);
                            break;
                        case "completate":
                            loadIntoListView_completate(s);
                            break;
                    }
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
                    while ((json = bufferedReader.readLine()) != null) {
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


    private void loadIntoListView_in_corso(String json) throws JSONException {
        ArrayList<String> tubeLines = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject line = jsonArray.getJSONObject(i);
            String lineName = "Corso : " + line.optString("titolo_corso") + "\t / Professore : " + line.optString("id_professore") + "\nData : " + line.optString("data") + "\t / Ora : " + line.optString("ora");
            tubeLines.add(lineName);
        }
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_in_corso);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_ripetizioni_in_corso = new MyRecyclerViewAdapter_ripetizioni_in_corso(getContext(), tubeLines);
        adapter_ripetizioni_in_corso.setClickListener(this);
        recyclerView.setAdapter(adapter_ripetizioni_in_corso);
    }

    private void loadIntoListView_completate(String json) throws JSONException {
        ArrayList<String> tubeLines = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject line = jsonArray.getJSONObject(i);
            String lineName = "Corso : " + line.optString("titolo_corso") + "\t / Professore : " + line.optString("id_professore") + "\nData : " + line.optString("data") + "\t / Ora : " + line.optString("ora");
            tubeLines.add(lineName);
        }
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_completate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), tubeLines);
        recyclerView.setAdapter(adapter);
    }


    private void loadIntoListView_annullate(String json) throws JSONException {
        ArrayList<String> tubeLines = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject line = jsonArray.getJSONObject(i);
            String lineName = "Corso : " + line.optString("titolo_corso") + "\t / Professore : " + line.optString("id_professore") + "\n Data : " + line.optString("data") + "\t / Ora : " + line.optString("ora");
            tubeLines.add(lineName);
        }
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_annullate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter(getContext(), tubeLines);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) { // Cliccando sul bottone "Disdici" o "Completata" --> Il fragment viene ricaricato per far visualizzare correttamente
        // le rispettive prenotazioni nella RecyclerView di loro appartenenza.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
}