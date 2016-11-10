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
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.OrderItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Class.OrderLoadTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.OrderDetailActivity;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPurchaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPurchaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPurchaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyPurchaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPurchaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPurchaseFragment newInstance(String param1, String param2) {
        MyPurchaseFragment fragment = new MyPurchaseFragment();
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
    private List listOrders;
    ArrayAdapter mAdapter;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllPurchasedOrderByAccountId.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_purchase, container, false);

        ListView listView = (ListView) view.findViewById(R.id.MyPurchaseListView);
        swipeContainer = (SwipeRefreshLayout)view. findViewById(R.id.swipeContainer);
        listOrders = new ArrayList();
        class RefreshMethod implements IRefreshMethod
        {
            @Override
            public void refresh() {
                fetchShopAsync(0);
            }
        }
        mAdapter = new OrderItemAdapter(getActivity(),listOrders,new RefreshMethod(),getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetail((Order)parent.getItemAtPosition(position));
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchShopAsync(0);
            }
        });

        if(AppHelper.currentAccount != null) {
            OrderLoadTask task = new OrderLoadTask(url, Integer.valueOf(AppHelper.currentAccount.getAccountid()), 1, swipeContainer, mAdapter, getActivity());
            task.execute();
        }
        return view;
    }

    public void openDetail(Order order)
    {
        Intent act = new Intent(getActivity(),OrderDetailActivity.class);
        act.putExtra(Order.TAG_ORDERID,order.getOrderid());
        act.putExtra(Order.TAG_BUY,"0");
        act.putExtra(Order.TAG_RESPONDED,1);
        getActivity().startActivity(act);
    }

    public void fetchShopAsync(int page)
    {
        if(AppHelper.currentAccount != null)
        {

            OrderLoadTask task = new OrderLoadTask(url,Integer.valueOf(AppHelper.currentAccount.getAccountid()),1,swipeContainer,mAdapter,getActivity());
            task.execute((Void) null);
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
}
