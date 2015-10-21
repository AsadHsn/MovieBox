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

    String textReview;

    public ReviewDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_detail, container, false);
    }

    public static ReviewDetailFragment newInstance(String reviewsAll) {
        ReviewDetailFragment f = new ReviewDetailFragment();

        //   objIntentMovieBean=movbean;
        Bundle args = new Bundle();
        args.putString("Review", reviewsAll);
        f.setArguments(args);

        return f;
    }



    @Override
    public void onStart() {
        super.onStart();


        Bundle arguments = getArguments();
        if(arguments != null)
        {
            textReview=arguments.getString("Review");
        }

        // handle intent extras
//        Bundle extras = getActivity().getIntent().getSerializableExtra("REVIEWACT");
//        if(extras != null)
//        {
//            handleExtras(extras);
//        }

        String textData="Reviews";

        Intent intent = getActivity().getIntent();

        if(intent!=null) {

            String temptextReview = (String) intent.getSerializableExtra("REVIEWACT");
            if(temptextReview!=null )
            {textReview=temptextReview;}
            Log.v(ReviewDetailFragment.class.getSimpleName(), textData);
        }
        TextView objtextview = (TextView) getView().findViewById(R.id.review_text_all);
        objtextview.setText(textReview);

        //To fetch the data again specially the Rating of the Movie

    }
}
