package com.abd.classroom1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamResultViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamResultViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamResultViewerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listview;
    private List<ExamResultModel> l1;
    private ExamResultListAdapter examResultListAdapter;


    private OnFragmentInteractionListener mListener;

    public ExamResultViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExamResultViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamResultViewerFragment newInstance(List<ExamResultModel> tl1) {
        ExamResultViewerFragment fragment = new ExamResultViewerFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        fragment.setL1(tl1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //  mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_result_viewer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listview = (ListView) getActivity().findViewById(R.id.lv_exam_result);
        // l1 = new ArrayList<>();
        examResultListAdapter = new ExamResultListAdapter(getActivity(), l1);
        listview.setAdapter(examResultListAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentInteractionListener) activity;
        mListener.onFragmentInteraction(4);
    }

    @Override
    public void onDetach() {
        mListener.onFragmentInteraction(-4);
        super.onDetach();
        mListener = null;
    }

    public void updateExamResultContent() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                examResultListAdapter.notifyDataSetChanged();
            }
        });

    }

    public List<ExamResultModel> getL1() {
        return l1;
    }

    public void setL1(List<ExamResultModel> l1) {
        this.l1 = l1;
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
        //  void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(int fragmentID);
    }
}
