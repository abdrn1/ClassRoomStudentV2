package com.abd.classroom1;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esotericsoftware.kryonet.Client;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MonitorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private UserLogin iam;
    private String[] receivers;
    private int monitorsPerRow =2;
    private Client client;


    int defaultWidth = 30;
    int defaultHeight = 20;
    List<ScreenshotListener> listeners = new ArrayList<ScreenshotListener>();

    public MonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitor, container, false);
    }

    public void setUserLogin(UserLogin iam){
        this.iam = iam;
    }

    public void setReceivers(String[] receivers){
        this.receivers = receivers;
    }

    public void addScreenshotListener(ScreenshotListener listener){
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ViewTreeObserver observer = getView().getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    observer.removeOnGlobalLayoutListener(this);
                } else {
                    observer.removeGlobalOnLayoutListener(this);
                }
                int noOfMonitors = 0;
                if(receivers != null && receivers.length>0)
                    noOfMonitors =receivers.length;
                int noOfRows = (int)Math.ceil((double)noOfMonitors/monitorsPerRow);
                int count = 0;

                LinearLayout containerLayout = (LinearLayout) getView().findViewById(R.id.monitorContainer);
                int monitorWidth = containerLayout.getWidth()/monitorsPerRow;
                for (int i = 0; i < noOfRows; i++) {
                    LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    linearLayout.setLayoutParams(params);
                    linearLayout.setWeightSum(Math.min(noOfMonitors, monitorsPerRow));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    for (int j = 0; j < monitorsPerRow && count !=noOfMonitors; j++) {
                        MonitorView mv = new MonitorView(getFragmentManager(), getActivity().getApplicationContext(), iam.getUserID(), receivers[count], client);
                        mv.iam = iam;
                        MonitorFragment.this.addScreenshotListener(mv);
                        linearLayout.addView(mv.getView());
                        Log.i("ttt", "monitor created ... " + count);
                        count++;
                        mv.update();
                    }
                    containerLayout.addView(linearLayout);
                }

            }
        });



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
        listeners.clear();
        mListener = null;
    }

    public void screenshotReceived(ScreenshotMessage ob) {
        for(ScreenshotListener l: listeners){
            l.screenshotReceived(ob);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface ScreenshotListener{
        public void screenshotReceived(ScreenshotMessage msg);
    }
}
