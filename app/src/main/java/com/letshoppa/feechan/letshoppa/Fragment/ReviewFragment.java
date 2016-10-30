package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ReviewItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Review;
import com.letshoppa.feechan.letshoppa.Class.ReviewDataLoadTask;
import com.letshoppa.feechan.letshoppa.Class.ReviewShopLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        initializeDetailShop(view);
        return view;
    }
    Toko currentShop;
    Review currentReview;
    EditText reviewEditText;
    Button saveButton;
    RatingBar inputRatingBar;

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
            setReview(view);
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

    public void fetchReviewAsync(int page)
    {
        if(currentShop != null)
        {
            String url = AppHelper.domainURL + "/AndroidConnect/GetAllReviewsByTokoIdAndExAccountId.php";
            int tokoid = currentShop.getTokoid();
            ReviewShopLoadTask task = new ReviewShopLoadTask(url,tokoid,swipeContainer,mAdapter,getActivity());
            task.execute();
        }
    }


    private void setReview(View view)
    {
        reviewEditText = (EditText) view.findViewById(R.id.reviewEditText);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        inputRatingBar = (RatingBar) view.findViewById(R.id.inputRatingBar);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReview();
            }
        });

        if(AppHelper.currentAccount != null) {
            OneReviewTask task = new OneReviewTask();
            task.execute();
        }
    }

    public class RefreshMethod implements IRefreshMethod
    {

        @Override
        public void refresh() {

        }
    }

    private void saveReview()
    {
        String url= AppHelper.domainURL + "/AndroidConnect/PostPutReview.php";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();

        if(AppHelper.currentAccount != null && currentShop != null) {
            String accountid = AppHelper.currentAccount.getAccountid();
            String tokoid = String.valueOf(currentShop.getTokoid());
            String isireview = reviewEditText.getText().toString();
            String pointreview = String.valueOf((int) inputRatingBar.getRating());
            parameter.add(new BasicNameValuePair(Review.TAG_ACCOUNTID, accountid));
            parameter.add(new BasicNameValuePair(Review.TAG_TOKOID, tokoid));
            parameter.add(new BasicNameValuePair(Review.TAG_ISIREVIEW, isireview));
            parameter.add(new BasicNameValuePair(Review.TAG_POINTREVIEW, pointreview));
            UpdateDataTask task = new UpdateDataTask(url, parameter, getActivity());
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


    public class OneReviewTask extends AsyncTask<Void, Void, Boolean>
    {
        String url;
        String messagejson;
        int successjson;

        public OneReviewTask() {
            this.url = AppHelper.domainURL+"/AndroidConnect/GetOneReviewByAccountIdTokoId.php";
            messagejson="";
            successjson=-1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Review.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
            parameter.add(new BasicNameValuePair(Review.TAG_TOKOID, String.valueOf(currentShop.getTokoid())));
            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null)
            {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        JSONArray reviewArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject reviewObj = reviewArray.getJSONObject(0);
                        Review review = Review.GetReviewFromJson(reviewObj);
                        if (review != null)
                        {
                            currentReview = review;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    }
                    else if(successjson == 0)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }

                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if (AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    } else {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.NoConnection;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                reviewEditText.setEnabled(true);
                saveButton.setEnabled(true);
                if(successjson==1 && currentReview != null)
                {
                    reviewEditText.setText(currentReview.getIsireview());
                    inputRatingBar.setRating((float) currentReview.getPointreview());
                }
            }
            else
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
