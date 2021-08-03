package com.example.progetto_ium_tweb;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        TextView email = view.findViewById(R.id.text_mail);
        TextView nome = view.findViewById(R.id.text_nome);
        TextView cognome = view.findViewById(R.id.text_cognome);

        email.setText(getIntent().getExtras().getString("email"));
        nome.setText(getIntent().getExtras().getString("nome"));
        cognome.setText(getIntent().getExtras().getString("cognome"));

        mAppBarConfiguration = new AppBarConfiguration.Builder( //Fragment
                R.id.nav_corsi, R.id.nav_mie_prenotazioni, R.id.nav_prenota_ripetizione, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
