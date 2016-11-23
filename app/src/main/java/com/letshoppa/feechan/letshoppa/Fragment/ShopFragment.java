package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;
import com.letshoppa.feechan.letshoppa.ViewOneMapsActivity;
import com.letshoppa.feechan.letshoppa.ViewShopKontakActivity;
import com.letshoppa.feechan.letshoppa.ViewShopRekeningActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        initializeDetailShop(view);
        initializeButton(view);
        return view;
    }

    Toko currentShop;
    private void initializeButton(View view)
    {
        Button viewLocationShopButton = (Button) view.findViewById(R.id.viewShopLocationButton);
        Button shopownerButton = (Button) view.findViewById(R.id.shopownerbutton);

        viewLocationShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocationOnMap();
            }
        });
        shopownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOwner();
            }
        });

        Button bankAccountButton = (Button) view.findViewById(R.id.bankAccountButton);
        bankAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewBankAccountActivity();
            }
        });

        Button shopKontakButton = (Button) view.findViewById(R.id.shopKontakButton);
        shopKontakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewShopKontakActivity();
            }
        });

    }
    private void openViewBankAccountActivity()
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ViewShopRekeningActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }

    private void openViewShopKontakActivity()
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ViewShopKontakActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }


    private void viewLocationOnMap()
    {
        if(currentShop != null) {
            Intent intentView = new Intent(getActivity(), ViewOneMapsActivity.class);
            intentView.putExtra(AppHelper.TAG_LATITUDE, this.currentShop.getLatitude());
            intentView.putExtra(AppHelper.TAG_LONGITUDE, this.currentShop.getLongitude());
            intentView.putExtra(AppHelper.TAG_TITLE, this.currentShop.getNamatoko());
            startActivity(intentView);
        }
        else
        {
            Toast.makeText(getActivity(),getString(R.string.unknown_error),Toast.LENGTH_LONG).show();
        }
    }
    private void viewOwner()
    {
        if(currentShop!=null) {
            Intent openPersonIntent = new Intent(getActivity(), PersonActivity.class);
            openPersonIntent.putExtra(Account.TAG_ACCOUNTID, String.valueOf(currentShop.getAccountid()));
            startActivity(openPersonIntent);
        }
    }

    private void initializeDetailShop(View view)
    {
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop!=null) {
            ImageView shopImageView = (ImageView) view.findViewById(R.id.shopImageView);
            ImageLoadTask shopImageLoad = new ImageLoadTask(currentShop.getGambartoko(),shopImageView);
            shopImageLoad.execute();

            TextView shopNameTextView = (TextView) view.findViewById(R.id.shopNameTextView);
            TextView deskripsiText = (TextView) view.findViewById(R.id.deskripsiText);
            TextView namaKategoriTextView = (TextView) view.findViewById(R.id.namaKategoriTextView);
            TextView locationTextView = (TextView) view.findViewById(R.id.locationTextView);

            namaKategoriTextView.setText(currentShop.getNamajenis());
            shopNameTextView.setText(currentShop.getNamatoko());
            deskripsiText.setText(currentShop.getDeskripsitoko());
            locationTextView.setText(currentShop.getLokasitoko());
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
