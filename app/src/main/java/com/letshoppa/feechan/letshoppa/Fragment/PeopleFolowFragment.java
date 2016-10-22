package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.PeopleExpandableListAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.HeaderInfo;
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

 */
public class PeopleFolowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PeopleFolowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleFolowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleFolowFragment newInstance(String param1, String param2) {
        PeopleFolowFragment fragment = new PeopleFolowFragment();
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
        View view = inflater.inflate(R.layout.fragment_people_folow, container, false);
        peopleExListView = (ExpandableListView) view.findViewById(R.id.peopleExpandableListView);
        Intent i = getActivity().getIntent();
        accountid = i.getStringExtra(Account.TAG_ACCOUNTID);
        if (!accountid.isEmpty()) {
            setFollowingTask();
        }
        return view;
    }

    String accountid;
    PeopleExpandableListAdapter mAdapter;
    ExpandableListView peopleExListView;


    private void setFollowingTask()
    {
        if(AppHelper.currentAccount != null) {
            FollowingLoadTask task = new FollowingLoadTask(accountid);
            task.execute();
        }
    }
    private void openDetailPerson(Account account)
    {
        Intent openPersonIntent = new Intent(getActivity(),PersonActivity.class);
        openPersonIntent.putExtra(Account.TAG_ACCOUNTID,account.getAccountid());
        startActivity(openPersonIntent);
    }

    ArrayList<HeaderInfo> listMenu= new ArrayList<HeaderInfo>();
    HeaderInfo following = new HeaderInfo("Following");
    HeaderInfo followed = new HeaderInfo("Followed");
    ProgressDialog dialog;

    public class FollowingLoadTask extends AsyncTask<Void, Void, Boolean>
    {
        String url=AppHelper.domainURL + "/AndroidConnect/GetAllFollowingByAccountId.php";
        String messagejson;
        int successjson;

        String accountid;

        public FollowingLoadTask(String accountid)
        {
            dialog = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            messagejson = "";
            successjson = -1;
            this.accountid = accountid;
        }

        JSONArray listfollowingJSON = null;
        ArrayList<Account> listFollowing = new ArrayList<Account>();
        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, accountid));
            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    //get detail
                    if (successjson == 1) {
                        listfollowingJSON = json.getJSONArray(Account.TAG_ACCOUNT_CLASS);

                        for (int i = 0; i < listfollowingJSON.length(); i++) {
                            JSONObject accountobject = listfollowingJSON.getJSONObject(i);
                            Account followingaccount = Account.GetAccountFromJson(accountobject);
                            listFollowing.add(followingaccount);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if (AppHelper.Message != "")
                    {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if (!success)
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            following.setProductList(listFollowing);
            FollowersLoadTask task = new FollowersLoadTask(accountid);
            task.execute();
        }
    }

    public class FollowersLoadTask extends AsyncTask<Void, Void, Boolean> {
        String url = AppHelper.domainURL + "/AndroidConnect/GetAllFollowerByAccountId.php";
        String messagejson;
        int successjson;

        String accountid;

        public FollowersLoadTask(String accountid) {
            messagejson = "";
            successjson = -1;
            this.accountid = accountid;
        }

        JSONArray listfollowerJSON = null;
        ArrayList<Account> listFollower = new ArrayList<Account>();

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, accountid));
            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);

                    //get detail
                    if (successjson == 1) {
                        listfollowerJSON = json.getJSONArray(Account.TAG_ACCOUNT_CLASS);

                        for (int i = 0; i < listfollowerJSON.length(); i++) {
                            JSONObject accountobject = listfollowerJSON.getJSONObject(i);
                            Account followingaccount = Account.GetAccountFromJson(accountobject);
                            listFollower.add(followingaccount);
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
            if (!success) {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            followed.setProductList(listFollower);
            listMenu.clear();
            listMenu.add(following);
            listMenu.add(followed);
            mAdapter = new PeopleExpandableListAdapter(getActivity(), listMenu);
            peopleExListView.setAdapter(mAdapter);
            peopleExListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    openDetailPerson((Account) parent.getExpandableListAdapter().getChild(groupPosition, childPosition));
                    return false;
                }
            });
            dialog.dismiss();
        }
    }
}
