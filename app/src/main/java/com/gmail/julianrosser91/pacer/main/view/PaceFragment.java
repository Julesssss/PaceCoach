package com.gmail.julianrosser91.pacer.main.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.julianrosser91.pacer.R;

/**
 * A fragment containing the main Pace views
 */
public class PaceFragment extends Fragment {

    public PaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
