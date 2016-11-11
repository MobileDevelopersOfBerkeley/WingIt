package com.mdb.wingit.wingit;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AdventureLog extends AppCompatActivity, Fragment implements View.OnClickListener {

    private startOptions.OnFragmentInteractionListener mListener;
    private RecyclerView rv;
    private AdventureAdapter adapter;
    private ArrayList<Object> adventures;

    public AdventureLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * can have params that are kept even if even after this fragment is recreated by Android
     * add this into a bundle and setArguments(bundle)
     * @return A new instance of fragment startOptions.
     */
    public static AdventureLog newInstance() {
        AdventureLog fragment = new AdventureLog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AdventureAdapter(getApplicationContext(), adventures);
        adventures = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getView();
        LinearLayout op1 = (LinearLayout) v.findViewById(R.id.option1);
        LinearLayout op2 = (LinearLayout) v.findViewById(R.id.option2);
        LinearLayout op3 = (LinearLayout) v.findViewById(R.id.option3);
        op1.setOnClickListener(this);
        op2.setOnClickListener(this);
        op3.setOnClickListener(this);
        return inflater.inflate(R.layout.fragment_start_options, container, false);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.option1:
                //open food stuff
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    // what is the point of this ? ? can i just use onClick
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof startOptions.OnFragmentInteractionListener) {
            mListener = (startOptions.OnFragmentInteractionListener) context;
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
