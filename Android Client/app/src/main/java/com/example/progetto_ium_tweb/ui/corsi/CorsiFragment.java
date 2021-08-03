package com.example.progetto_ium_tweb.ui.corsi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progetto_ium_tweb.MyRecyclerViewAdapter_corsi;
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

public class CorsiFragment extends Fragment {

    MyRecyclerViewAdapter_corsi adapter;
    String json_id = "";
    UserSessionManager userSessionManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_corsi, container, false);
        userSessionManager = new UserSessionManager(getContext());
        json_id = userSessionManager.getUserSession();
        String url = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server?servlet=index&operazione=get_all_corsi";
        downloadJSON(url);
        return root;
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

    private void loadIntoListView(String json) throws JSONException {

        ArrayList<String> corsi = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject line = jsonArray.getJSONObject(i);
            String lineName = line.optString("titolo");
            corsi.add(lineName);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyRecyclerViewAdapter_corsi(getContext(), corsi);
        recyclerView.setAdapter(adapter);
    }

}
