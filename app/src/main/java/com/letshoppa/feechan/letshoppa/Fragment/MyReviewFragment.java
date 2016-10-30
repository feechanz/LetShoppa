package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.AdapterList.ReviewItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Review;
import com.letshoppa.feechan.letshoppa.Class.ReviewDataLoadTask;
import com.letshoppa.feechan.letshoppa.Class.ReviewShopLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyReviewFragment newInstance(String param1, String param2) {
        MyReviewFragment fragment = new MyReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_review, container, false);
        initializeDetailShop(view);
        return view;
    }

    Toko currentShop;

    RatingBar ratingBar;
    TextView scoreRateTextView;
    TextView countRateTextView;
    SwipeRefreshLayout swipeContainer;
    ArrayAdapter mAdapter;
    private void initializeDetailShop(View view)
    {
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop!=null) {
            ImageView shopImageView = (ImageView) view.findViewById(R.id.shopImageView);
            ImageLoadTask shopImageLoad = new ImageLoadTask(currentShop.getGambartoko(),shopImageView);
            shopImageLoad.execute();
            TextView shopNameTextView = (TextView) view.findViewById(R.id.shopNameTextView);
            shopNameTextView.setText(currentShop.getNamatoko());

            initializeReview(view);
        }
    }

    private void initializeReview(View view)
    {
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        scoreRateTextView = (TextView) view.findViewById(R.id.scoreRateTextView);
        countRateTextView = (TextView) view.findViewById(R.id.countRateTextView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        ListView myReviewsListView = (ListView) view.findViewById(R.id.MyReviewsListView);

        List listMyReviews = new ArrayList();;


        mAdapter = new ReviewItemAdapter(getActivity(), listMyReviews);
        myReviewsListView.setAdapter(mAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchReviewAsync(0);
            }
        });

        if(currentShop != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/GetReviewData.php";
            int tokoid = currentShop.getTokoid();
            ReviewDataLoadTask task = new ReviewDataLoadTask(url, scoreRateTextView, countRateTextView, ratingBar, tokoid, getActivity());
            task.execute();
        }

        myReviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPerson((Review)parent.getItemAtPosition(position));
            }
        });
        fetchReviewAsync(0);
    }

    public void openPerson(Review review)
    {
        Intent openPersonIntent = new Intent(getActivity(),PersonActivity.class);
        openPersonIntent.putExtra(Account.TAG_ACCOUNTID, String.valueOf(review.getAccountid()));
        startActivity(openPersonIntent);
    }

    public void fetchReviewAsync(int page) {
        if (currentShop != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/GetAllReviewsByTokoIdAndExAccountId.php";
            int tokoid = currentShop.getTokoid();
            ReviewShopLoadTask task = new ReviewShopLoadTask(url, tokoid, swipeContainer, mAdapter, getActivity());
            task.execute();
        }
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
