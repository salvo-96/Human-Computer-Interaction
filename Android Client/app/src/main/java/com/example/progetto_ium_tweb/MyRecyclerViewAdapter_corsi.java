package com.example.progetto_ium_tweb;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter_corsi extends RecyclerView.Adapter<MyRecyclerViewAdapter_corsi.ViewHolder>{

    private List<String> mData; // Lista che sara' riempita con l'ArrayList<String> del MainActivity che passo al costruttore di "MyRecyclerViewAdapter" .
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MyRecyclerViewAdapter_corsi(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_corsi, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String corsi = mData.get(position);
        holder.myTextView.setText(corsi);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.row_corsi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }



    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}