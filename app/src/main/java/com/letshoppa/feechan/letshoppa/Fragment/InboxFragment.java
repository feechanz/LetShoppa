package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.InboxItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Pesan;
import com.letshoppa.feechan.letshoppa.Class.PesanLoadTask;
import com.letshoppa.feechan.letshoppa.R;
import com.letshoppa.feechan.letshoppa.ViewPesanActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
    private List listPesans;
    ArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(getString(R.string.message));
        View view =  inflater.inflate(R.layout.fragment_inbox, container, false);

        ListView listView = (ListView) view.findViewById(R.id.MessageListView);
        swipeContainer = (SwipeRefreshLayout)view. findViewById(R.id.swipeContainer);
        listPesans = new ArrayList();
        mAdapter = new InboxItemAdapter(getActivity(),listPesans);
        listView.setAdapter(mAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchShopAsync(0);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPesan((Pesan)parent.getItemAtPosition(position));
            }
        });
        fetchShopAsync(0);
        return view;
    }

    private void openPesan(Pesan pesan)
    {
        Intent viewPesanAct = new Intent(getActivity(),ViewPesanActivity.class);
        viewPesanAct.putExtra(Pesan.TAG_PESAN,pesan);
        startActivity(viewPesanAct);
    }
    private void fetchShopAsync(int page)
    {
        if(AppHelper.currentAccount!=null) {
            String url= AppHelper.domainURL + "/AndroidConnect/GetAllInboxMessageByAccountid.php";
            int accountid = Integer.valueOf(AppHelper.currentAccount.getAccountid());
            PesanLoadTask task = new PesanLoadTask(url, accountid, mAdapter, getActivity(), swipeContainer);
            task.execute();
        }
    }

}
