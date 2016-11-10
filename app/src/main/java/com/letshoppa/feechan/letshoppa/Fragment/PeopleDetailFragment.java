package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.letshoppa.feechan.letshoppa.Class.Pesan;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;
import com.letshoppa.feechan.letshoppa.SendMessageActivity;
import com.letshoppa.feechan.letshoppa.ViewContactActivity;

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

        if (accountid != "") {
            AccountLoadTask task = new AccountLoadTask(accountid, view);
            task.execute();
        }
        return view;
    }

    TextView mPremiumaServiceTextView;
    TextView mNameTextView;
    TextView mBirthdateTextView;
    TextView mGenderTextView;
    TextView mEmailTextView;
    ImageView mProfileImageView;
    Button mFollowBtn;

    Button mViewContactButton;
    Button mSendMessageButton;

    private void initializeViewProfile(View view) {
        if (currentAccount != null) {
            mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
            mBirthdateTextView = (TextView) view.findViewById(R.id.birthdateTextView);
            mGenderTextView = (TextView) view.findViewById(R.id.genderTextView);
            mPremiumaServiceTextView = (TextView) view.findViewById(R.id.premiumServiceTextView);
            mEmailTextView = (TextView) view.findViewById(R.id.emailTextView);
            mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);
            mFollowBtn = (Button) view.findViewById(R.id.followButton);

            mViewContactButton = (Button) view.findViewById(R.id.viewContactButton);
            mSendMessageButton = (Button) view.findViewById(R.id.sendMessageButton);

            mNameTextView.setText(currentAccount.getNama());
            mBirthdateTextView.setText(currentAccount.getBirthdate().toString());
            mGenderTextView.setText(currentAccount.getGender());
            mPremiumaServiceTextView.setText(currentAccount.getPremiumaccount().toString());
            mEmailTextView.setText(currentAccount.getEmail());

            mViewContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewContact();
                }
            });
            mSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessage();
                }
            });

            ImageLoadTask imageTask = new ImageLoadTask(currentAccount.getLinkgambaraccount(), mProfileImageView);
            imageTask.execute();
        }
    }

    private void ViewContact()
    {
        if(AppHelper.currentAccount != null)
        {
            int accountid = Integer.valueOf(currentAccount.getAccountid());
            Intent viewContactAct = new Intent(getActivity(),ViewContactActivity.class);
            viewContactAct.putExtra(Account.TAG_ACCOUNTID,accountid);
            startActivity(viewContactAct);
        }
    }

    private void SendMessage()
    {
        if(currentAccount != null)
        {
            int penerimaaccountid= Integer.valueOf(currentAccount.getAccountid());
            String namapenerima=currentAccount.getNama();
            Intent sendPesanAct = new Intent(getActivity(),SendMessageActivity.class);
            sendPesanAct.putExtra(Pesan.TAG_PENERIMAACCOUNTID,penerimaaccountid);
            sendPesanAct.putExtra(Pesan.TAG_NAMAPENERIMA,namapenerima);
            startActivity(sendPesanAct);
        }
    }

    ProgressDialog dialog;

    public class AccountLoadTask extends AsyncTask<Void, Void, Boolean> {

        private final String url_refresh_account = AppHelper.domainURL + "/AndroidConnect/GetAccountByAccountId.php";
        private final String accountid;

        View view;
        String messagejson;
        int successjson;

        public AccountLoadTask(String accountid, View view) {
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
                    if (successjson == 1) {
                        JSONArray accountArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject accountObj = accountArray.getJSONObject(0);
                        Account account = Account.GetAccountFromJson(accountObj);
                        if (account != null) {
                            currentAccount = account;
                            return true;
                        } else {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
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
            if (success) {
                if (currentAccount != null) {
                    initializeViewProfile(view);
                    getActivity().setTitle(currentAccount.getNama());
                }

            } else {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();

            }
            if (AppHelper.currentAccount == null) {
                dialog.dismiss();
            } else {
                CekFollowTask task = new CekFollowTask();
                task.execute();
                //cek follow
            }
        }
    }

    public class CekFollowTask extends AsyncTask<Void, Void, Boolean> {

        String messagejson;
        int successjson;

        public CekFollowTask()
        {
            messagejson="";
            successjson=-1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(currentAccount == null || AppHelper.currentAccount == null)
            {
                messagejson=getString(R.string.unknown_error);
                return false;
            }
            else if(Integer.valueOf(currentAccount.getAccountid()) == Integer.valueOf(AppHelper.currentAccount.getAccountid()))
            {
                return true;
            }
            else
            {
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
                parameter.add(new BasicNameValuePair(Account.TAG_TARGETACCOUNTID, currentAccount.getAccountid()));
                String url_get_follow = AppHelper.domainURL + "/AndroidConnect/GetOneFollowingByAccountId.php";
                JSONObject json = AppHelper.GetJsonObject(url_get_follow, "POST", parameter);
                if (json != null)
                {
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
                        else if(successjson == 0)
                        {
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
                        if (AppHelper.Message != "") {
                            messagejson = AppHelper.Message;
                        } else {
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

        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(success)
            {
                if(successjson==1)
                {
                    mFollowBtn.setText(getString(R.string.Unfollow));
                    mFollowBtn.setEnabled(true);
                    mFollowBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unFollowAction();
                        }
                    });
                    //add delete task
                }
                else if(successjson==0)
                {
                    mFollowBtn.setText(getString(R.string.Follow));
                    mFollowBtn.setEnabled(true);
                    mFollowBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FollowAction();
                        }
                    });
                    //add follow task
                }
            }
            else
            {
                mFollowBtn.setEnabled(false);
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }

    }

    public  class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            CekFollowTask task = new CekFollowTask();
            task.execute();
        }
    }

    private void unFollowAction()
    {
        String url = AppHelper.domainURL + "/AndroidConnect/DeleteFollow.php";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();

        parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
        parameter.add(new BasicNameValuePair(Account.TAG_TARGETACCOUNTID, currentAccount.getAccountid()));

        UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity(),new RefreshMethod());
        task.execute();
    }

    private void FollowAction()
    {
        String url = AppHelper.domainURL + "/AndroidConnect/AddFollow.php";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();

        parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
        parameter.add(new BasicNameValuePair(Account.TAG_TARGETACCOUNTID, currentAccount.getAccountid()));

        UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity(), new RefreshMethod());
        task.execute();

    }


}
