package com.letshoppa.feechan.letshoppa.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.PengumumanItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.PengumumanItem;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    //

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();


        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private List listPengumuman;
    private SwipeRefreshLayout swipeContainer;
    ArrayAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //String[] ShopTipeArray = {"Toko Fashion & Aksesoris","Toko Pakaian","Toko Elektronik","Toko Mainain & Hobi", "Toko DVD/CD Film, Musik, Game"};

        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.fragment_home,ShopTipeArray);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //ListView listView = (ListView) view.findViewById(R.id.announceListView);


        ListView listView = (ListView) view.findViewById(R.id.announceListView);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listPengumuman = new ArrayList();
        listPengumuman.add(new PengumumanItem("Hari ini kalian bisa belanja gratis","Administrator"));
        listPengumuman.add(new PengumumanItem("Kalo belanja dibeli yaa","Administrator"));
        listPengumuman.add(new PengumumanItem("Diskon 50% dari toko Ganteng","Toko Ganteng"));


        mAdapter = new PengumumanItemAdapter(getActivity(),listPengumuman);
        listView.setAdapter(mAdapter);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchHomeAsync(0);
            }
        });
        // Inflate the layout for this fragment
        return view;//inflater.inflate(R.layout.fragment_home, container, false);
    }
    public void fetchHomeAsync(int page) {

        HomeRefreshTask test = new HomeRefreshTask(swipeContainer);
        test.execute((Void) null);

    }

    public class HomeRefreshTask extends AsyncTask<Void, Void, Boolean> {

        SwipeRefreshLayout swipeContainer;
        public HomeRefreshTask(SwipeRefreshLayout swipeContainer) {
            this.swipeContainer = swipeContainer;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try
            {
                Thread.sleep(2000);

                listPengumuman = new ArrayList();
                listPengumuman.add(new PengumumanItem("Hari ini kalian bisa belanja gratis","Administrator"));
                listPengumuman.add(new PengumumanItem("Kalo belanja dibeli yaa","Administrator"));
                listPengumuman.add(new PengumumanItem("Diskon 50% dari toko Ganteng","Toko Ganteng"));
                listPengumuman.add(new PengumumanItem("Kalo belanja dibeli yaa","Administrator"));
                listPengumuman.add(new PengumumanItem("Diskon 50% dari toko Ganteng","Toko Ganteng"));


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                mAdapter.clear();
                mAdapter.addAll(listPengumuman);
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
