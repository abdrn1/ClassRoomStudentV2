package com.abd.classroom1;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleMonitorFragment extends Fragment {

    Bitmap bm;

    public SingleMonitorFragment() {
        // Required empty public constructor
    }

    public void setScreenshot(Bitmap bm){
        this.bm = bm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_single_monitor, container, false);
        if(bm != null){
            ImageView im = (ImageView) view.findViewById(R.id.screenView);
            im.setImageBitmap(bm);
        }
        return view;
    }

}
