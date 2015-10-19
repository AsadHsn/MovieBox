package com.example.nid.moviebox;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewDetailFragment extends Fragment {


    public ReviewDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_detail, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        String textData="Reviews";

        Intent intent = getActivity().getIntent();

        textData = (String) intent.getSerializableExtra("REVIEW");
        Log.v(ReviewDetailFragment.class.getSimpleName(), textData);

        TextView objtextview=(TextView) getView().findViewById(R.id.review_text_all);




        objtextview.setText(textData);

        //To fetch the data again specially the Rating of the Movie

    }
}
