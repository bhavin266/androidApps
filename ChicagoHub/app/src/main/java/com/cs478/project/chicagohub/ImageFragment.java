package com.cs478.project.chicagohub;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Restaurant Image fragment is used to show image of selected restuarant
 * Activities use displayImage() function with a index parameter to choose the
 * correct image to be displayed
 */
public class ImageFragment extends Fragment {
    private static final String TAG = "ImageFragment";

    private int currentIndex=-1;

    public ImageFragment() {
        // Required empty public constructor
    }


    public static ImageFragment newInstance(int resourceListId) {
        ImageFragment myFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt("resourceListId", resourceListId);
        myFragment.setArguments(args);
        return myFragment;
    }


    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, getClass().getSimpleName() + ":entered onCreateView()");
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }


    public void displayImage(int newIndex) {
        Log.d(TAG, getClass().getSimpleName() + ":called displayImage() with index:"+newIndex);
        ImageView image = (ImageView) getActivity().findViewById(R.id.imageView);
        TypedArray imageList = getResources().obtainTypedArray(getArguments().getInt("resourceListId"));
        if (newIndex < 0 || newIndex >= imageList.length())
            return;
        currentIndex = newIndex;
        image.setImageResource(imageList.getResourceId(currentIndex, -1));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String index);
    }
}
