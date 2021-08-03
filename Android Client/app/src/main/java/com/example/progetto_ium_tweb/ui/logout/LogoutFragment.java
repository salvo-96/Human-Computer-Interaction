package com.example.progetto_ium_tweb.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.progetto_ium_tweb.R;
import com.example.progetto_ium_tweb.UserSessionManager;
import com.example.progetto_ium_tweb.ui.corsi.CorsiFragment;

public class LogoutFragment  extends Fragment{

    UserSessionManager userSessionManager;
     Button btn_si;
     Button btn_no;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_logout, container, false);
        btn_si = (Button) root.findViewById(R.id.button1);
        btn_no = (Button) root.findViewById(R.id.button2);
        btn_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSessionManager = new UserSessionManager(getContext());
                userSessionManager.logoutUser(getContext());
                Toast.makeText(getActivity().getApplicationContext(),"A PRESTO !!",Toast.LENGTH_LONG).show();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new CorsiFragment();
                replaceFragment(fragment);
            }
        });
        return root;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
