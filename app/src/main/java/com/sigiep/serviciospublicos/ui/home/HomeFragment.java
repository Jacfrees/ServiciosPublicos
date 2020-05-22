package com.sigiep.serviciospublicos.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sigiep.serviciospublicos.R;
import com.sigiep.serviciospublicos.RegistrarLecturaFragment;
import com.sigiep.serviciospublicos.controllers.ArchivoController;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View vista;
    Button btnRegistrar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        btnRegistrar = root.findViewById(R.id.btn_registrar_lectura);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getActivity(), "AAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();

                RegistrarLecturaFragment fragment = new RegistrarLecturaFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.child_fragment, fragment, "registrarLecturaFragment");
                ft.addToBackStack(null);  //opcional, si quieres agregarlo a la pila
                ft.commit(); */

                //tartActivity(new Intent(getActivity(), RegistrarLecturaFragment.class)); //REDIRIGE AL REGISTRAR

                ArchivoController obj = new ArchivoController();
                obj.importarArchivo();

        }
        });

        return root;
    }
}
