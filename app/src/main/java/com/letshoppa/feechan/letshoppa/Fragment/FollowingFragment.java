package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.letshoppa.feechan.letshoppa.AdapterList.PeopleExpandableListAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.HeaderInfo;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance(String param1, String param2) {
        FollowingFragment fragment = new FollowingFragment();
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

    PeopleExpandableListAdapter mAdapter;
    private ArrayList<HeaderInfo> headerList;
    private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_following, container, false);
        final ExpandableListView peopleExListView = (ExpandableListView) view.findViewById(R.id.peopleExpandableListView);

        ArrayList<HeaderInfo> listMenu;
        listMenu = new ArrayList<HeaderInfo>();
        HeaderInfo following = new HeaderInfo("Following");
        HeaderInfo followed = new HeaderInfo("Followed");

        ArrayList<Account> listFollowing = new ArrayList<Account>();
        ArrayList<Account> listFollowed = new ArrayList<Account>();

        listFollowing.add(AppHelper.currentAccount);
        listFollowing.add(AppHelper.currentAccount);
        listFollowing.add(AppHelper.currentAccount);

        listFollowed.add(AppHelper.currentAccount);
        listFollowed.add(AppHelper.currentAccount);

        following.setProductList(listFollowing);
        followed.setProductList(listFollowed);

        listMenu.add(following);
        listMenu.add(followed);

        mAdapter = new PeopleExpandableListAdapter(getActivity(), listMenu);
        peopleExListView.setAdapter(mAdapter);
        peopleExListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                openDetailPerson((Account) parent.getExpandableListAdapter().getChild(groupPosition,childPosition));
                return false;
            }
        });
        return view;
    }
    private void openDetailPerson(Account account)
    {
        Intent openPersonIntent = new Intent(getActivity(),PersonActivity.class);
        openPersonIntent.putExtra(Account.TAG_ACCOUNTID,account.getAccountid());
        startActivity(openPersonIntent);
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
