package com.example.progetto_ium_tweb;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.example.progetto_ium_tweb.UserSessionManager;


public class MyRecyclerViewAdapter_ripetizioni_in_corso extends RecyclerView.Adapter<MyRecyclerViewAdapter_ripetizioni_in_corso.ViewHolder> implements Adapter {

    private List<String> mData; // Lista che sara' riempita con l'ArrayList<String> del Fragment MieRipetizioniFragment che passo al costruttore di "MyRecyclerViewAdapter".

    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    UserSessionManager userSessionManager;

    String json_id;

    String data_da_inviare = "";

    String ora_da_inviare = "";

    final static String KEY_OPERAZIONE_DISDICI = "disdici_ripetizione";

    final static String KEY_OPERAZIONE_COMPLETA_RIPETIZIONE = "completa_ripetizione";


    public MyRecyclerViewAdapter_ripetizioni_in_corso(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        userSessionManager = new UserSessionManager(context);
        json_id = userSessionManager.getUserSession();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_ripetizioni, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String prof = mData.get(position);
        holder.myTextView.setText(prof);
    }

    // NUMERO TOTALE DI RIGHE NELLA LISTA
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTextView;
        Button button_completa;
        Button button_disdici;

        ViewHolder(View itemView) {
            super(itemView);
            button_disdici = itemView.findViewById(R.id.button_disdici);
            button_completa = itemView.findViewById(R.id.button_completata);
            myTextView = itemView.findViewById(R.id.row_ripetizioni);
            itemView.setOnClickListener(this);
            button_disdici.setOnClickListener(this);
            button_completa.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
            data_da_inviare = myTextView.getText().toString().substring(myTextView.getText().toString().
                    indexOf("Data") + 7, myTextView.getText().toString().length() - 16); //Estraggo la data dalla riga che ho cliccato dalla lista di prenotazioni in corso
            data_da_inviare = data_da_inviare.replaceAll("\\s+", ""); //PULISCO EVENTUALI whiteSpace \n ; \t; ecc..
            ora_da_inviare = myTextView.getText().toString().substring(myTextView.getText().toString().
                    indexOf("Ora") + 6);//Estraggo l'ora dalla riga che ho cliccato dalla lista di prenotazioni in corso
            ora_da_inviare = ora_da_inviare.replaceAll("\\s+", "");
            if (view.getId() == button_disdici.getId()) { // LISTENER DEL BOTTONE PER DISDIRE UNA RIPETIZIONE
                String url_disdici_prenotazione = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + json_id + "?servlet=user" +
                        "&operazione=disdici&data=" + data_da_inviare + "&ora=" + ora_da_inviare;
                downloadJSON(url_disdici_prenotazione, KEY_OPERAZIONE_DISDICI);
            } else if (view.getId() == button_completa.getId()) { // LISTENER DEL BOTTONE PER COMPLETARE UNA RIPETIZIONE
                String url_completa_prenotazione = "http://" + userSessionManager.getIp() + ":8080/Progetto-IUM-TWEB/servlet_server;jsessionid=" + json_id + "?servlet=user" +
                        "&operazione=completata&data=" + data_da_inviare + "&ora=" + ora_da_inviare;
                downloadJSON(url_completa_prenotazione, KEY_OPERAZIONE_COMPLETA_RIPETIZIONE);
            }
        }
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
                switch (key_operazione) {
                    case "disdici_ripetizione":
                        loadIntoListView_disdici(s);
                        break;
                    case "completa_ripetizione":
                        loadIntoListView_completa_ripetizione(s);
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

    private void loadIntoListView_disdici(String s) {
        if (s.equals("true") && s != null)
            Toast.makeText(mInflater.getContext(), "Prenotazione annullata!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mInflater.getContext(), "Impossibile annullare la prenotazione!", Toast.LENGTH_SHORT).show();
    }


    private void loadIntoListView_completa_ripetizione(String s) {
        if (s.equals("true") && s != null)
            Toast.makeText(mInflater.getContext(), "Prenotazione Completata!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mInflater.getContext(), "Impossibile completare prenotazione!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return 0;
    }

    public String getItem(int id) {
        return mData.get(id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}