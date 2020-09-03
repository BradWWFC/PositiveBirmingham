package com.example.finalyearproject.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finalyearproject.R;

public class TabFragment extends Fragment {
    private String webString;
    int color;

    public TabFragment(String webString) {
        this.webString = webString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        TextView text = view.findViewById(R.id.text);
        text.setText(webString);

        return view;
    }
}
