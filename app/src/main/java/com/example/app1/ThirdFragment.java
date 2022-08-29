package com.example.app1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {//@link Fragment} subclass.
 * Use the {//@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {
    Button camera, gallery;
    ImageView imageView;
    TextView result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*camera = View.findViewById(R.id.button);
        gallery = View.findViewById(R.id.button2);

        result = View.findViewById(R.id.result);
        imageView = View.findViewById(R.id.imageView);*/
        return inflater.inflate(R.layout.clasificacion_fragment, container, false);
    }
}