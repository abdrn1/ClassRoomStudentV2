package com.abd.classroom1;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.GenericArrayType;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CmImageViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CmImageViewerFragment extends Fragment {

    private ImageView imgView;
    private String imagePath;
    private int imgWidth = 100;
    private int imgHeight = 100;


    private OnFragmentInteractionListener mListener;

    public CmImageViewerFragment() {
        // Required empty public constructor
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cm_image_viewer, container, false);


    }

    public void showImage(String path) {
        Bitmap tempImg = ScalDownImage.decodeSampledBitmapFromResource(path, 100, 100);
        imgView.setImageBitmap(tempImg);
    }

    public void showImage(Bitmap img) {
        imgView.setImageBitmap(img);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgView = (ImageView) getActivity().findViewById(R.id.img_curr_image);
        ImageButton btnZoomin = (ImageButton) getActivity().findViewById(R.id.btn_zoomin);
        ImageButton btnZoomout = (ImageButton) getActivity().findViewById(R.id.btn_zoomout);
        GeneralUtil.buttonEffect(btnZoomin);
        GeneralUtil.buttonEffect(btnZoomout);
        btnZoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgHeight += 40;
                    imgWidth += 40;

                    Bitmap tm = ScalDownImage.decodeSampledBitmapFromResource(imagePath, imgWidth, imgHeight);
                    imgView.setImageBitmap(tm);
                } catch (Exception ex) {

                }
            }
        });


        btnZoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgHeight += 40;
                    imgWidth += 40;
                    Bitmap tm = ScalDownImage.decodeSampledBitmapFromResource(imagePath, imgWidth, imgHeight);
                    imgView.setImageBitmap(tm);

                } catch (Exception ex) {

                }
            }

        });

        showImage(imagePath);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
