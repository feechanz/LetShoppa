package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PeopleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleDetailFragment newInstance(String param1, String param2) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
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

    Account currentAccount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people_detail, container, false);
        Intent i = getActivity().getIntent();
        String accoountId = i.getStringExtra(Account.TAG_ACCOUNTID);

        //currentAccount = (Account) i.getSerializableExtra(Account.TAG_ACCOUNT);

        /*if(currentAccount != null)
        {
            getActivity().setTitle(currentAccount.getNama());
        }*/
        return view;
    }


}
