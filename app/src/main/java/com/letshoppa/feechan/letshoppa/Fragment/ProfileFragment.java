package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.letshoppa.feechan.letshoppa.Class.UploadLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Utility;
import com.letshoppa.feechan.letshoppa.Interface.IUploadFinalMethod;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    TextView mPremiumaServiceTextView;
    TextView mNameTextView ;
    TextView mBirthdateTextView ;
    TextView mGenderTextView ;
    ImageView mProfileImageView;

    private void initializeProfile(View view)
    {
        mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        mBirthdateTextView = (TextView) view.findViewById(R.id.birthdateTextView);
        mGenderTextView = (TextView) view.findViewById(R.id.genderTextView);
        mPremiumaServiceTextView = (TextView) view.findViewById(R.id.premiumServiceTextView);
        mProfileImageView = (ImageView) view.findViewById(R.id.profileImageView);
        Button mChangeProfileImageButton = (Button) view.findViewById(R.id.changePictureButton);
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        //ivImage = mProfileImageView;
        //txView = mNameTextView;

        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePicture();
            }
        });
        mChangeProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePicture();
            }
        });
        if(AppHelper.currentAccount != null)
        {
            initializeViewProfile();
        }
    }

    private void initializeViewProfile()
    {
        mNameTextView.setText(AppHelper.currentAccount.getNama());
        mBirthdateTextView.setText(AppHelper.currentAccount.getBirthdate().toString());
        mGenderTextView.setText(AppHelper.currentAccount.getGender());
        mPremiumaServiceTextView.setText(AppHelper.currentAccount.getPremiumaccount().toString());

        ImageLoadTask imageTask = new ImageLoadTask(AppHelper.currentAccount.getLinkgambaraccount(),mProfileImageView);
        imageTask.execute();
    }
    private void refreshDrawerPicture()
    {
        ImageLoadTask imageTask = new ImageLoadTask(AppHelper.currentAccount.getLinkgambaraccount(),AppHelper.profilePicture);
        imageTask.execute();
    }

    private void changeProfilePicture()
    {
        selectImage();
    }


    private int REQUEST_CAMERA=0;
    private int SELECT_FILE=1;
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    //TextView txView;
    //ImageView ivImage;
    String userChoosenTask="";
    //ProgressBar progressBar;
    String url_upload_pp = AppHelper.domainURL+"/AndroidConnect/UploadProfilePicture.php";

    private void upload_PP(String path)
    {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if(AppHelper.currentAccount != null) {
            param.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
            UploadLoadTask task = new UploadLoadTask(url_upload_pp, Account.TAG_PROFILEPICTURE, path, param,getActivity(),new LastMethod());
            task.execute();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_Library),
                getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.change_picture));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals(getString(R.string.take_photo))) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals(getString(R.string.choose_from_Library))) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bm = AppHelper.getResizedBitmap(bm,300,300);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ivImage.setImageBitmap(bm);
        //txView.setText(destination.getPath());
        upload_PP(destination.getPath());
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = AppHelper.getResizedBitmap(thumbnail,300,300);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ivImage.setImageBitmap(thumbnail);

        upload_PP(destination.getPath());
        //txView.setText(data.getData().getPath());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        initializeProfile(view);
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
    private class LastMethod implements IUploadFinalMethod
    {
        @Override
        public void finalMethod() {
            UserRefreshTask task = new UserRefreshTask();
            task.execute();
        }
    }

    public class UserRefreshTask extends AsyncTask<Void, Void, Boolean> {

        private final String url_refresh_account = AppHelper.domainURL+"/AndroidConnect/GetAccountByAccountId.php";
        private final String mAccountid;

        String messagejson;
        int successjson;

        UserRefreshTask() {
            mAccountid = AppHelper.currentAccount.getAccountid();
            messagejson = "";
            successjson = -1;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, mAccountid));
            JSONObject json = AppHelper.GetJsonObject(url_refresh_account, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        JSONArray accountArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject accountObj = accountArray.getJSONObject(0);
                        Account account = Account.GetAccountFromJson(accountObj);
                        if (account != null)
                        {
                            AppHelper.currentAccount = account;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    } else {
                        return false;
                    }
                } catch (JSONException e)
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
                AppHelper.createLoginSession();
                AppHelper.getAccountFromSession();
                if(AppHelper.currentAccount != null) {
                    initializeViewProfile();
                    if (AppHelper.profilePicture != null) {
                        refreshDrawerPicture();
                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(), messagejson, Toast.LENGTH_SHORT).show();
            }
        }

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
