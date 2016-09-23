package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.TipeShoppingItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Jenistoko;
import com.letshoppa.feechan.letshoppa.ListShopActivity;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RootShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RootShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RootShoppingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RootShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RootShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RootShoppingFragment newInstance(String param1, String param2) {
        RootShoppingFragment fragment = new RootShoppingFragment();
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

    private List listTipeShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_shopping, container, false);
        // Inflate the layout for this fragment
        final ListView listView = (ListView) view.findViewById(R.id.tipeShopListView);

        listTipeShop = new ArrayList();
        listTipeShop.add(new Jenistoko(1,"Toko Fashion & Aksesoris"));
        listTipeShop.add(new Jenistoko(2,"Toko Pakaian"));
        listTipeShop.add(new Jenistoko(3,"Toko Elektronik"));
        listTipeShop.add(new Jenistoko(4,"Toko Mainan & Hobi"));
        listTipeShop.add(new Jenistoko(5,"Toko DVD/CD Film, Musik, Game"));

        ArrayAdapter mAdapter = new TipeShoppingItemAdapter(getActivity(),listTipeShop);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Jenistoko item = (Jenistoko) listTipeShop.get(position);
                AppHelper.tipeShopItem = item;
                startActivity(new Intent(getActivity(),ListShopActivity.class));
            }
        });
        return view;
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
