package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
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
        String accountid = i.getStringExtra(Account.TAG_ACCOUNTID);

        //currentAccount = (Account) i.getSerializableExtra(Account.TAG_ACCOUNT);

        /*if(currentAccount != null)
        {
            getActivity().setTitle(currentAccount.getNama());
        }*/
        if(accountid != "" ) {
            AccountLoadTask task = new AccountLoadTask(accountid, view);
            task.execute();
        }
        return view;
    }

    TextView mPremiumaServiceTextView;
    TextView mNameTextView ;
    TextView mBirthdateTextView ;
    TextView mGenderTextView ;
    ImageView mProfileImageView;


    private void initializeViewProfile(View view)
    {
        if(currentAccount != null) {
            mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
            mBirthdateTextView = (TextView) view.findViewById(R.id.birthdateTextView);
            mGenderTextView = (TextView) view.findViewById(R.id.genderTextView);
            mPremiumaServiceTextView = (TextView) view.findViewById(R.id.premiumServiceTextView);
            mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);

            mNameTextView.setText(currentAccount.getNama());
            mBirthdateTextView.setText(currentAccount.getBirthdate().toString());
            mGenderTextView.setText(currentAccount.getGender());
            mPremiumaServiceTextView.setText(currentAccount.getPremiumaccount().toString());
            ImageLoadTask imageTask = new ImageLoadTask(currentAccount.getLinkgambaraccount(), mProfileImageView);
            imageTask.execute();
        }
    }


    public class AccountLoadTask extends AsyncTask<Void, Void, Boolean> {

        private final String url_refresh_account = AppHelper.domainURL+"/AndroidConnect/GetAccountByAccountId.php";
        private final String accountid;
        ProgressDialog dialog;
        View view;
        String messagejson;
        int successjson;
        public AccountLoadTask(String accountid,View view) {
            this.accountid = accountid;
            this.view = view;
            messagejson = "";
            successjson = -1;

            dialog = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, accountid));
            JSONObject json = AppHelper.GetJsonObject(url_refresh_account, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        JSONArray accountArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject accountObj = accountArray.getJSONObject(0);
                        Account account = Account.GetAccountFromJson(accountObj);
                        if (account != null)
                        {
                            currentAccount = account;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if (AppHelper.Message != "")
                    {
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
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                if(currentAccount != null) {
                    initializeViewProfile(view);
                    getActivity().setTitle(currentAccount.getNama());
                }
            }
            else
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}
