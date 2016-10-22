package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.PengumumanItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AddPostTask;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Post;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.EditPostActivity;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.PersonActivity;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //listPengumuman.add(new PengumumanItem("Hari ini kalian bisa belanja gratis","Administrator"));
        //listPengumuman.add(new PengumumanItem("Kalo belanja dibeli yaa","Administrator"));
        //listPengumuman.add(new PengumumanItem("Diskon 50% dari toko Ganteng","Toko Ganteng"));


        mAdapter = new PengumumanItemAdapter(getActivity(),listPengumuman);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Post item = (Post)parent.getItemAtPosition(position);
                detailPost(item);
                return true;
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchHomeAsync(0);
            }
        });
        initializeHome(view);
        fetchHomeAsync(0);
        // Inflate the layout for this fragment
        return view;//inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void detailPost(final Post post)
    {
        boolean self = false;
        if(AppHelper.currentAccount != null) {
            if (post.getAccountid() == Integer.valueOf(AppHelper.currentAccount.getAccountid())) {
                self = true;
            }
        }
        if(!self)
        {
            final CharSequence[] items = { getString(R.string.view_person),getString(R.string.cancel) };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(getString(R.string.your_post));
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    switch (item)
                    {
                        case 0:
                            Intent openPersonIntent = new Intent(getActivity(),PersonActivity.class);
                            openPersonIntent.putExtra(Post.TAG_ACCOUNTID, String.valueOf(post.getAccountid()));
                            startActivity(openPersonIntent);
                            break;
                        default:

                    }
                }

            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            final CharSequence[] items = { getString(R.string.edit), getString(R.string.delete),getString(R.string.cancel) };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(getString(R.string.action));
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int item)
                {
                    switch (item)
                    {
                        case 0:
                            editPost(post);
                            break;
                        case 1:
                            deletePost(post);
                            break;
                        default:

                    }
                }

            });

            AlertDialog alert = builder.create();

            alert.show();
        }
    }
    private void editPost(Post post)
    {
        Intent openPostEditIntent = new Intent(getActivity(),EditPostActivity.class);
        openPostEditIntent.putExtra(Post.TAG_POST,post);
        startActivity(openPostEditIntent);
    }
    private void deletePost(final Post post)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        String url= AppHelper.domainURL + "/AndroidConnect/DeletePost.php";
                        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                        parameter.add(new BasicNameValuePair(Post.TAG_POSTID,String.valueOf(post.getPostid())));
                        UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity(),new PostRefresh());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.prompt_delete_post)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
    private void initializeHome(final View view)
    {
        if(!AppHelper.isLoggedIn() || AppHelper.currentAccount == null)
        {
            ImageButton sendImageBtn = (ImageButton) view.findViewById(R.id.sendImgButton);
            TextView sendTextView = (TextView) view.findViewById(R.id.sendTextView);
            postEditText = (EditText) view.findViewById(R.id.postEditText);

            sendImageBtn.setVisibility(View.GONE);
            sendTextView.setVisibility(View.GONE);
            postEditText.setVisibility(View.GONE);


        }
        else
        {
            postEditText = (EditText) view.findViewById(R.id.postEditText);
            ImageButton sendImageBtn = (ImageButton) view.findViewById(R.id.sendImgButton);
            sendImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPost(view);
                }
            });
        }

    }

    EditText postEditText;
    private void sendPost(View view)
    {
        postEditText = (EditText) view.findViewById(R.id.postEditText);
        String tulisan = postEditText.getText().toString();
        String url = AppHelper.domainURL + "/AndroidConnect/AddPost.php";
        int accountid=0;
        boolean cancel = false;
        if(TextUtils.isEmpty(tulisan))
        {
            postEditText.requestFocus();
            postEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(AppHelper.currentAccount != null)
        {
            accountid = Integer.valueOf(AppHelper.currentAccount.getAccountid());
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if(!cancel)
        {
            AddPostTask task = new AddPostTask(url,accountid,tulisan,getActivity(),new PostRefresh());
            task.execute();

        }
    }
    public class PostRefresh implements IRefreshMethod
    {

        @Override
        public void refresh() {
            fetchHomeAsync(0);
            postEditText.setText("");
        }
    }
    public void fetchHomeAsync(int page) {

        HomeRefreshTask test = new HomeRefreshTask(swipeContainer);
        test.execute((Void) null);

    }

    public class HomeRefreshTask extends AsyncTask<Void, Void, Boolean> {

        String messagejson;
        int successjson;

        String url;
        SwipeRefreshLayout swipeContainer;
        JSONArray myposts = null;
        public HomeRefreshTask(SwipeRefreshLayout swipeContainer) {
            this.swipeContainer = swipeContainer;
            successjson=-1;
            messagejson="";
            listPengumuman = new ArrayList();
            this.url = AppHelper.domainURL + "/AndroidConnect/GetAllPostByAccountId.php";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String accountid;

            if(AppHelper.isLoggedIn() && AppHelper.currentAccount != null)
            {
                accountid = AppHelper.currentAccount.getAccountid();
            }
            else
            {
                accountid = "0";
            }
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, String.valueOf(accountid)));

            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json != null)
            {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        myposts = json.getJSONArray(Post.TAG_POST);

                        for (int i=0;i<myposts.length();i++)
                        {
                            JSONObject postobject = myposts.getJSONObject(i);
                            Post newpost = Post.GetPostFromJson(postobject);
                            listPengumuman.add(newpost);
                        }
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if(AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }

            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }

        }
        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                mAdapter.clear();
                mAdapter.addAll(listPengumuman);

            }
            else
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            if(swipeContainer != null) {
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
