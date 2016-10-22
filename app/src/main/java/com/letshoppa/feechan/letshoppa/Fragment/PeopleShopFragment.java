package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Intent;
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

import com.letshoppa.feechan.letshoppa.AdapterList.ShopItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.MyShopActivity;
import com.letshoppa.feechan.letshoppa.R;
import com.letshoppa.feechan.letshoppa.ShopActivity;

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
 * to handle interaction events.
 * Use the {@link PeopleShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PeopleShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleShopFragment newInstance(String param1, String param2) {
        PeopleShopFragment fragment = new PeopleShopFragment();
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

    private SwipeRefreshLayout swipeContainer;
    private List listMyShop;
    ArrayAdapter mAdapter;

    private void initialize(View view)
    {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        ListView listView = (ListView) view.findViewById(R.id.MyShopListView);
        listMyShop = new ArrayList();

        mAdapter = new ShopItemAdapter(getActivity(),listMyShop);
        listView.setAdapter(mAdapter);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchShopAsync(0);
            }
        });

        ShopRefreshLoadTask loadTask = new ShopRefreshLoadTask(url,Integer.valueOf(accountid),swipeContainer);
        loadTask.execute((Void) null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailShop((Toko)parent.getItemAtPosition(position));
            }
        });
    }

    private void openDetailShop(Toko toko)
    {
        if(Integer.valueOf(AppHelper.currentAccount.getAccountid())== Integer.valueOf(toko.getAccountid()))
        {
            Intent openShopIntent = new Intent(getActivity(), MyShopActivity.class);
            openShopIntent.putExtra(Toko.TAG_TOKO, toko);
            startActivity(openShopIntent);
        }
        else {
            Intent openShopIntent = new Intent(getActivity(), ShopActivity.class);
            openShopIntent.putExtra(Toko.TAG_TOKO, toko);
            startActivity(openShopIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_people_shop, container, false);
        Intent i = getActivity().getIntent();
        accountid = i.getStringExtra(Account.TAG_ACCOUNTID);

        initialize(view);

        return view;
    }


    public String url = AppHelper.domainURL+"/AndroidConnect/GetAllTokoByAccountId.php";
    public String accountid;
    public void fetchShopAsync(int page)
    {
        ShopRefreshLoadTask loadTask = new ShopRefreshLoadTask(url,Integer.valueOf(accountid),swipeContainer);
        loadTask.execute((Void) null);
    }

    public class ShopRefreshLoadTask extends AsyncTask<Void, Void, Boolean>
    {
        SwipeRefreshLayout swipeContainer;
        private String url;
        private int accountid;

        String messagejson;
        int successjson;

        public ShopRefreshLoadTask(String url, int accountid, SwipeRefreshLayout swipeContainer) {
            this.url = url;
            this.accountid = accountid;
            this.swipeContainer = swipeContainer;
            successjson=-1;
            messagejson="";
            shops = new ArrayList();

        }
        JSONArray myshops = null;
        List shops;

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, String.valueOf(accountid)));

            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json != null)
            {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        myshops = json.getJSONArray(Toko.TAG_TOKO);

                        for (int i=0;i<myshops.length();i++)
                        {
                            JSONObject tokoobject = myshops.getJSONObject(i);
                            Toko newtoko = Toko.GetTokoFromJson(tokoobject);
                            shops.add(newtoko);
                        }
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
                    if(AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
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
                mAdapter.addAll(shops);
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
}
