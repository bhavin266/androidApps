package com.cs478.project.chicagohub;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link ListFragment.OnListFragmentInteractionListener}
 * interface.
 */

public class ListFragment extends android.app.ListFragment {

    private static final String TAG = "ListFragment";
    private ArrayList<PlaceInformation> resourceList = new ArrayList<>();
    private int mCurrIdx = -1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    public static ListFragment newInstance(int resourceListId) {
        ListFragment myFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt("resourceListId", resourceListId);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initResourceList();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //Initialize resourcelist to be used in ListFragment
    // resource list could be either restaurants list or hotels list
    private void initResourceList() {
        resourceList.clear();                   // cleanup list first
        for (String s : getResources().getStringArray(getArguments().getInt("resourceListId"))) {
            String temp[] = s.split("\n");
            if (temp.length == 2)
                resourceList.add(new PlaceInformation(temp[0], temp[1]));
            else
                resourceList.add(new PlaceInformation(temp[0], ""));     //In case address is missing, we keep in blank

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Make sure activity using it has implemented OnListFragmentInteractionListener
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedState);
        /*
        * Using android's predefined two_line_list_item layout to avoid writing a custom adaptor class
        * Will override getView to show name and address of places in two lines
        * */
        setListAdapter(new ArrayAdapter<PlaceInformation>(getActivity(),
                android.R.layout.two_line_list_item, android.R.id.text1, resourceList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(resourceList.get(position).getName());
                text2.setText(resourceList.get(position).getAddress());
                return view;
            }

        });
       /* setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.fragment_list_item, getResources().getStringArray(getArguments().getInt("resourceListId")) ));
*/
        // Allow one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (mCurrIdx != -1) {
            getListView().setItemChecked(mCurrIdx, true);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {

        // Indicates the selected item has been checked
        getListView().setItemChecked(pos, true);
        ;
        mListener.onListSelection(pos);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        resourceList = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        public void onListSelection(int index);
    }
}

/**
 * Class that will hold information of places
 * This is done to separate out information model from code
 **/
class PlaceInformation {
    String name;
    String address;

    PlaceInformation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }
}
