package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyShopDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyShopDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShopDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyShopDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShopDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShopDetailFragment newInstance(String param1, String param2) {
        MyShopDetailFragment fragment = new MyShopDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_shop_detail, container, false);
        initializeDetailShop(view);
        initializeButton(view);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initializeLocation(View view)
    {
        EditText locationEditText = (EditText) view.findViewById(R.id.shopLocationEditText);
        TextView latLongTextView = (TextView) view.findViewById(R.id.latlongTextView);

        locationEditText.setText(currentShop.getLokasitoko());
        latLongTextView.setText(currentShop.getLatitude()+","+currentShop.getLongitude());

    }
    private void initializeButton(final View view)
    {
        ImageButton editActiveImgBtn = (ImageButton) view.findViewById(R.id.editImageBtn);
        editActiveImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClicked(view);
            }
        });

        ImageButton saveLocationImgBtn = (ImageButton) view.findViewById(R.id.saveLocationImageBtn);
        saveLocationImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLocationClicked(view);
            }
        });
    }
    private void editLocationClicked(View view)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //run save location
                        Toast.makeText(getActivity(),getString(R.string.success),Toast.LENGTH_LONG).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.prompt_change_location)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
    Toko currentShop;

    boolean activeEdit;
    EditText shopNameEditText;
    EditText deskripsiText;
    TextView namaKategoriTextView;
    private void editClicked(View view)
    {
        activeEdit = !activeEdit;
        if(activeEdit)
        {
            setToSave(view);
        }
        else
        {
            setToEdit(view);
        }
        setActiveControl();
    }
    private void setToEdit(View view)
    {
        //save data
        TextView editTextView = (TextView) view.findViewById(R.id.editTextView);
        ImageButton editImageButton = (ImageButton) view.findViewById(R.id.editImageBtn);

        editTextView.setText(getString(R.string.Edit));
        editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
    }
    private void setToSave(View view)
    {
        TextView editTextView = (TextView) view.findViewById(R.id.editTextView);
        ImageButton editImageButton = (ImageButton) view.findViewById(R.id.editImageBtn);

        editTextView.setText(getString(R.string.Save));
        editImageButton.setImageResource(android.R.drawable.ic_menu_save);
    }
    private void setActiveControl()
    {
        shopNameEditText.setEnabled(activeEdit);
        deskripsiText.setEnabled(activeEdit);
    }


    private void initializeDetailShop(View view)
    {
        activeEdit=false;
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop!=null) {
            ImageView shopImageView = (ImageView) view.findViewById(R.id.shopImageView);
            ImageLoadTask shopImageLoad = new ImageLoadTask(currentShop.getGambartoko(),shopImageView);
            shopImageLoad.execute();

            shopNameEditText = (EditText) view.findViewById(R.id.shopNameEditText);
            deskripsiText = (EditText) view.findViewById(R.id.deskripsiText);
            namaKategoriTextView = (TextView) view.findViewById(R.id.namaKategoriTextView);

            //change activity title
            getActivity().setTitle(currentShop.getNamatoko());

            namaKategoriTextView.setText(currentShop.getNamajenis());
            shopNameEditText.setText(currentShop.getNamatoko());
            deskripsiText.setText(currentShop.getDeskripsitoko());

            setActiveControl();
            initializeLocation(view);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
