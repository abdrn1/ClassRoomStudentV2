package com.abd.classroom1;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamViewerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MYID = 5;
    List<QuestionItem> examQuestions;
    int pagecount = 1;
    int numberOfPages = 0;
    // ToDo : Not to be saved variables
    TextView tvNumberofpages;
    private RecyclerView mRecyclerView;
    private ExamRowAdapter mAdapter;
    private Client client;
    private UserLogin iam;
    String examFileName;

    private RecyclerView.LayoutManager mLayoutManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ExamViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamViewerFragment newInstance(String param1, String param2) {
        ExamViewerFragment fragment = new ExamViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String getExamFileName() {
        return examFileName;
    }

    public void setExamFileName(String examFileName) {
        this.examFileName = examFileName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public UserLogin getIam() {
        return iam;
    }

    public void setIam(UserLogin iam) {
        this.iam = iam;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // hide the action bar of the activity
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam_viewer, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.examlist);
        tvNumberofpages = (TextView) v.findViewById(R.id.tv_numberofpages);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        ImageButton btnNext = (ImageButton) v.findViewById(R.id.btn_next);
        ImageButton btnPrev = (ImageButton) v.findViewById(R.id.btn_prev);
        Button btnFinsihExam = (Button) v.findViewById(R.id.btn_finsih_exam);
        GeneralUtil.buttonEffect(btnNext);
        GeneralUtil.buttonEffect(btnPrev);
        mAdapter = new ExamRowAdapter(this.getActivity(), null);
        mAdapter.setQuestionsList(makePages(examQuestions, pagecount));
        mRecyclerView.setAdapter(mAdapter);
        updatePageText();
        btnFinsihExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishExam();

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagecount < numberOfPages) {
                    pagecount++;
                    updatePageText();
                    mAdapter.setQuestionsList(makePages(examQuestions, pagecount));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });


                }

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagecount > 1) {
                    pagecount--;
                    updatePageText();
                    mAdapter.setQuestionsList(makePages(examQuestions, pagecount));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        // aa.inflateMenu(R.menu.abd_menu);
        // Inflate the layout for this fragment
        return v;
    }


    private void updateQuestionsList(List<QuestionItem> list) {
        this.examQuestions = list;
        Log.d("Exam Ifo", "Number of Question Equal: " + Integer.toString(examQuestions.size()));
        pagecount = 1;
        mAdapter.setQuestionsList(makePages(examQuestions, pagecount));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

        updatePageText();
    }

    public void setQuestionsList(List<QuestionItem> list) {
        this.examQuestions = list;

    }

    private void updatePageText() {
        tvNumberofpages.setText(Integer.toString(pagecount) + "/" + Integer.toString(numberOfPages));

    }

    private void finishExam() {
        ExamResult result = ExamResultCalculator.CalculateExamResult(examQuestions);
        Log.d("re", "Exam Result = : " + Double.toString(result.getStudentMark()));
        ExamResultMessage erm = new ExamResultMessage();
        erm.setSenderID(iam.getUserID());
        erm.setSenderName(iam.getUserName());
        erm.setReceivers(new String[]{"100"});
        erm.examFileName = examFileName;

        erm.setExamresult(result.getExaMmark());
        erm.setStudentresult(result.getStudentMark());
        if (client != null) {
            if (client.isConnected()) {
                client.sendTCP(erm);
                Log.d("re", "Send Exam Result to Teacher");

            } else {

                try {
                    SendUtil.reConnect(client, iam);
                    Log.d("re", "Send Exam Result to Teacher");
                    client.sendTCP(erm);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "There is No Connection With Server", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }

        mListener.displayExamResult(erm);
        mListener.retuntoPreviousScreen(MYID);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public List<QuestionItem> makePages(List<QuestionItem> ql, int pgCount) {

        if (pgCount == 1) {
            double xx = ql.size();
            xx = xx / 5;
            xx = Math.ceil(xx);
            Log.d("info xx", Double.toString((ql.size() / 5)));
            numberOfPages = (int) xx;
        }
        int oLSize = ql.size();
        int startAt = (pgCount * 5) - 5;
        int endAt = startAt + 5;
        List<QuestionItem> tempList = new ArrayList<>();


        for (int i = startAt; ((i < endAt) && (i < oLSize)); i++) {
            tempList.add(ql.get(i));
        }


        return tempList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mListener = (OnFragmentInteractionListener) activity;
    }

    public void setmListener(Activity activity) {
        this.mListener = (OnFragmentInteractionListener) activity;

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //  public void onExamViewerInteraction(Uri uri);
        public void retuntoPreviousScreen(int FRAGMENTID);
        public void displayExamResult(ExamResultMessage erm);
    }

}
