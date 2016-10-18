package com.letshoppa.feechan.letshoppa.Fragment;

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
import android.widget.ListView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.PeopleItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleFragment newInstance(String param1, String param2) {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_people, container, false);
        // Inflate the layout for this fragment
        initialize(view);
        return view;
    }
    SwipeRefreshLayout swipeContainer;
    private List listPeople;
    ArrayAdapter mAdapter;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllAccount.php";
    public void initialize(View view)
    {
        ListView listView = (ListView) view.findViewById(R.id.PeopleListView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listPeople = new ArrayList();
        mAdapter = new PeopleItemAdapter(getActivity(),listPeople);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailPerson((Account)parent.getItemAtPosition(position));
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchShopAsync(0);
            }
        });

        PeopleLoadTask task = new PeopleLoadTask(swipeContainer,url);
        task.execute();
    }

    private void openDetailPerson(Account account)
    {
        Intent openPersonIntent = new Intent(getActivity(),PersonActivity.class);
        openPersonIntent.putExtra(Account.TAG_ACCOUNTID,account.getAccountid());
        startActivity(openPersonIntent);
    }
    public void fetchShopAsync(int page)
    {

        PeopleLoadTask task = new PeopleLoadTask(swipeContainer,url);
        task.execute((Void) null);

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public class PeopleLoadTask extends AsyncTask<Void, Void, Boolean>
    {
        SwipeRefreshLayout swipeContainer;
        private String url;

        String messagejson;
        int successjson;

        JSONArray peopleListJSON = null;
        List peopleList = new ArrayList();

        public PeopleLoadTask(SwipeRefreshLayout swipeContainer, String url) {
            this.swipeContainer = swipeContainer;
            this.url = url;
            this.messagejson = "";
            successjson = -1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        peopleListJSON = json.getJSONArray(Account.TAG_ACCOUNT_CLASS);

                        for (int i = 0; i < peopleListJSON.length(); i++) {
                            JSONObject productobject = peopleListJSON.getJSONObject(i);
                            Account person = Account.GetAccountFromJson(productobject);
                            peopleList.add(person);
                        }
                        return true;
                    } else {
                        return false;
                    }
                } catch (JSONException e) {
                    successjson = 3;
                    if (AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    } else {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            } else {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                mAdapter.clear();
                mAdapter.addAll(peopleList);
            }
            else
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            //set refresh off
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
        }
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
}
