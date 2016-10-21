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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.MyShopProductItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.ProductRefreshLoadTask;
import com.letshoppa.feechan.letshoppa.AddProductsActivity;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyShopProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyShopProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShopProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public MyShopProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShopProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShopProductFragment newInstance(String param1, String param2) {
        MyShopProductFragment fragment = new MyShopProductFragment();
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

    Toko currentShop;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllProductByTokoId.php";
    private SwipeRefreshLayout swipeContainer;
    private List listMyProducts;
    ArrayAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_shop_product, container, false);
        initializeProductFragment(view);
        initializeButton(view);
        return view;
    }

    private void initializeProductFragment(View view)
    {
        //get current shop
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        ListView listView = (ListView) view.findViewById(R.id.MyProductsListView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listMyProducts = new ArrayList();
        mAdapter = new MyShopProductItemAdapter(getActivity(),listMyProducts);

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
        ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, getActivity());
        loadTask.execute((Void) null);
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

    private void initializeButton(View view)
    {
        Button addButton = (Button) view.findViewById(R.id.add_product_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddButton();
            }
        });
    }
    private void clickAddButton()
    {
        Intent addProductAct = new Intent(getActivity(),AddProductsActivity.class);
        addProductAct.putExtra(Toko.TAG_TOKO,currentShop);
        startActivity(addProductAct);
        return;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        fetchShopAsync(0);
    }
    /*
    public class MyProductRefreshLoadTask extends AsyncTask<Void, Void, Boolean> {
        SwipeRefreshLayout swipeContainer;
        private String url;
        private int tokoid;

        String messagejson;
        int successjson;

        public MyProductRefreshLoadTask(String url, int tokoid, SwipeRefreshLayout swipeContainer) {
            this.url = url;
            this.tokoid = tokoid;
            this.swipeContainer = swipeContainer;
            successjson = -1;
            messagejson = "";
            products = new ArrayList();

        }

        JSONArray myproducts = null;
        List products;

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            BasicNameValuePair n = new BasicNameValuePair(Produk.TAG_TOKOID, String.valueOf(tokoid));
            parameter.add(n);

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        myproducts = json.getJSONArray(Produk.TAG_PRODUK);

                        for (int i = 0; i < myproducts.length(); i++) {
                            JSONObject productobject = myproducts.getJSONObject(i);
                            Produk newproduk = Produk.GetProdukFromJson(productobject);
                            products.add(newproduk);
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
                mAdapter.addAll(products);
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
    */
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
