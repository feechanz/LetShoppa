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
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.AdapterList.ProductRefreshLoadTask;
import com.letshoppa.feechan.letshoppa.AdapterList.ShopProductItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Produk;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShopProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopProductFragment newInstance(String param1, String param2) {
        ShopProductFragment fragment = new ShopProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop_product, container, false);
        initializeDetailShop(view);
        initializeProductFragment(view);
        return view;
    }

    private void initializeDetailShop(View view) {
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);
        if(currentShop!=null) {
            TextView shopNameTextView = (TextView) view.findViewById(R.id.shopNameTextView);
            shopNameTextView.setText(currentShop.getNamatoko());

            //change activity title
            getActivity().setTitle(currentShop.getNamatoko());
        }
    }

    Toko currentShop;
    SwipeRefreshLayout swipeContainer;
    private List listMyProducts;
    ArrayAdapter mAdapter;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllProductByTokoId.php";

    private void initializeProductFragment(View view)
    {
        //get current shop
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        ListView listView = (ListView) view.findViewById(R.id.MyProductsListView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listMyProducts = new ArrayList();
        mAdapter = new ShopProductItemAdapter(getActivity(),listMyProducts);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailProduct((Produk)parent.getItemAtPosition(position));
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
        ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, getActivity());
        loadTask.execute((Void) null);
    }
    private void openDetailProduct(Produk produk)
    {
        /*
        Intent openProdukIntent = new Intent(getActivity(),ShopActivity.class);
        openProdukIntent.putExtra(Produk.TAG_PRODUK,produk);
        startActivity(openProdukIntent);
        */
    }
    public void fetchShopAsync(int page)
    {
        if(currentShop != null) {
            ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, getActivity());
            loadTask.execute((Void) null);
        }
        else
        {
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
