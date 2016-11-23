package com.letshoppa.feechan.letshoppa.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.JenistokoAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.GpsService;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Jenistoko;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Class.UploadLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Utility;
import com.letshoppa.feechan.letshoppa.Interface.IUploadFinalMethod;
import com.letshoppa.feechan.letshoppa.ProductReportActivity;
import com.letshoppa.feechan.letshoppa.R;
import com.letshoppa.feechan.letshoppa.ShopKontakActivity;
import com.letshoppa.feechan.letshoppa.ShopOrderActivity;
import com.letshoppa.feechan.letshoppa.ShopRekeningActivity;
import com.letshoppa.feechan.letshoppa.ViewOneMapsActivity;

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
        initializeSpinnerStatus(view);
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

    EditText locationEditText;
    TextView latLongTextView;
    private void initializeLocation(View view)
    {
        locationEditText = (EditText) view.findViewById(R.id.shopLocationEditText);
        latLongTextView = (TextView) view.findViewById(R.id.latlongTextView);

        locationEditText.setText(currentShop.getLokasitoko());
        latLongTextView.setText(currentShop.getLatitude()+","+currentShop.getLongitude());

        latitudeShop = currentShop.getLatitude();
        longitudeShop = currentShop.getLongitude();

    }
    private void initializeButton(final View view)
    {
        ImageButton editActiveImgBtn = (ImageButton) view.findViewById(R.id.editImageBtn);
        viewCurrentShopLocationBtn = (Button) view.findViewById(R.id.viewSelectedLocationButton);


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
        viewCurrentShopLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocationOnMap();
            }
        });

        Button setLocationBtn = (Button) view.findViewById(R.id.set_location_button);
        setLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation(view);
            }
        });
        Button changePictureBtn = (Button) view.findViewById(R.id.changePictureButton);
        changePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture(view);
            }
        });
        ImageButton saveStatusImgBtn = (ImageButton) view.findViewById(R.id.saveStatusImageBtn);
        saveStatusImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus(view);
            }
        });

        Button viewProductReportButton = (Button) view.findViewById(R.id.viewProductReportButton);
        viewProductReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductReportActivity(view);
            }
        });
        Button viewOrderReportButton = (Button) view.findViewById(R.id.viewOrderReportButton);
        viewOrderReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrderReportActivity(view);
            }
        });

        Button bankAccountButton = (Button) view.findViewById(R.id.bankAccountButton);
        bankAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBankAccountActivity(view);
            }
        });

        Button shopKontakButton = (Button) view.findViewById(R.id.shopKontakButton);
        shopKontakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopKontakActivity(view);
            }
        });
    }
    private void openShopKontakActivity(View view)
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ShopKontakActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }

    private void openBankAccountActivity(View view)
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ShopRekeningActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }

    private void openProductReportActivity(View view)
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ProductReportActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }

    private void openOrderReportActivity(View view)
    {
        if(currentShop != null)
        {
            Intent intent = new Intent(getActivity(), ShopOrderActivity.class);
            intent.putExtra(Toko.TAG_TOKO, currentShop);
            startActivity(intent);
        }
    }

    private void saveStatus(View view)
    {
        Spinner spinner = (Spinner) view.findViewById(R.id.statusshopspinner);
        Jenistoko selected = (Jenistoko) spinner.getSelectedItem();
        if(selected != null && currentShop != null)
        {
            dialogSaveStatus(selected);
        }
    }

    private void dialogSaveStatus(final Jenistoko selected)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status

                        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                        String statusbaru = String.valueOf(selected.getJenistokoid());
                        parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentShop.getTokoid())));
                        parameter.add(new BasicNameValuePair(Toko.TAG_STATUSTOKO, statusbaru));

                        String url = AppHelper.domainURL + "/AndroidConnect/PutTokoStatus.php";
                        UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.prompt_save_status)+"?")
                .setPositiveButton(getActivity().getString(R.string.yes),dialogClickListener).
                setNegativeButton(getActivity().getString(R.string.no),dialogClickListener).show();
    }

    private void initializeSpinnerStatus(View view)
    {
        List listJenisToko = new ArrayList();
        listJenisToko.add(new Jenistoko(1,getString(R.string.buka)));
        listJenisToko.add(new Jenistoko(2,getString(R.string.tutup)));

        final ArrayAdapter mAdapter = new JenistokoAdapter(getActivity(), listJenisToko);
        Spinner spinner = (Spinner) view.findViewById(R.id.statusshopspinner);
        spinner.setAdapter(mAdapter);

        if(currentShop!=null)
        {
            if(currentShop.getStatustoko() == 1)
            {
                spinner.setSelection(0);
            }
            else
            {
                spinner.setSelection(1);
            }
        }
    }

    GpsService gps;
    double latitudeShop;
    double longitudeShop;
    private void setLocation(View viwe)
    {
        if(gps== null) {
            gps = new GpsService(getActivity());
        }
        gps.getLocation();
        if(gps.isCanUseGps())
        {
            if(gps.isCanGetLocation())
            {
                latitudeShop = gps.getLatitude();
                longitudeShop = gps.getLongitude();
                LocationAddresTask task = new LocationAddresTask(latitudeShop,longitudeShop);
                //set enable
                task.execute((Void) null);
            }
            else
            {
                Toast.makeText(getActivity(),getString(R.string.waiting_for_location),Toast.LENGTH_SHORT).show();
            }
            gps.stopUsingGPS();
        }
        else
        {
            Toast.makeText(getActivity(),getString(R.string.error_cant_get_location),Toast.LENGTH_SHORT).show();
            gps.showSettingAlert();
        }
    }

    private final String TAG_RESULTS = "results";
    private final String TAG_FORMATTED_ADDRESS = "formatted_address";
    public class LocationAddresTask extends AsyncTask<Void, Void, Boolean> {
        private final double latitude;
        private final double longitude;
        public String result;
        public String resultLangLong;
        ProgressDialog dialog;

        LocationAddresTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;

            dialog = ProgressDialog.show(getActivity(), "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
        }
        @Override
        protected Boolean doInBackground(Void... params)
        {
            result = "";
            resultLangLong="";
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";
            //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=-6.886216599999999,107.5807599&sensor=true";
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            // Getting JSON String from URL

            JSONObject json = AppHelper.GetJsonObject(url, "GET", parameter);
            if (json != null) {
                try {
                    JSONArray resultArray = json.getJSONArray(TAG_RESULTS);
                    if (resultArray.length() > 0) {
                        JSONObject jsonObject = resultArray.getJSONObject(0);
                        result = jsonObject.getString(TAG_FORMATTED_ADDRESS) ;
                        resultLangLong = getString(R.string.Coordinate) + " : " + latitude + "," + longitude;

                    } else {
                        result = getString(R.string.error_cant_get_location);
                    }
                } catch (JSONException e) {
                    result = getString(R.string.error_json);
                }

            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            dialog.dismiss();
            if (success) {
                Toast.makeText(getActivity(), getString(R.string.selected) + " : " + result, Toast.LENGTH_LONG).show();
                latLongTextView.setText(resultLangLong);
                locationEditText.setText(result);
            }
            else
            {
                Toast.makeText(getActivity(), getString(R.string.error_cant_get_location), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
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
                        //execute task
                        //Toast.makeText(getActivity(),getString(R.string.success),Toast.LENGTH_LONG).show();
                        saveLocation();
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
    Button viewCurrentShopLocationBtn;
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

    private void saveLocation()
    {
        if(currentShop != null) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            String locationBaru = locationEditText.getText().toString();
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentShop.getTokoid())));
            parameter.add(new BasicNameValuePair(Toko.TAG_LOKASITOKO, locationBaru));
            parameter.add(new BasicNameValuePair(Toko.TAG_LATITUDE, String.valueOf(latitudeShop)));
            parameter.add(new BasicNameValuePair(Toko.TAG_LONGITUDE, String.valueOf(longitudeShop)));

            String url = AppHelper.domainURL + "/AndroidConnect/PutTokoLocation.php";
            UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity());
            task.execute();
        }
    }

    private void setToEdit(View view)
    {
        //save data
        TextView editTextView = (TextView) view.findViewById(R.id.editTextView);
        ImageButton editImageButton = (ImageButton) view.findViewById(R.id.editImageBtn);

        editTextView.setText(getString(R.string.Edit));
        editImageButton.setImageResource(android.R.drawable.ic_menu_edit);

        if(currentShop != null) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            String namaBaru = ((TextView) view.findViewById(R.id.shopNameEditText)).getText().toString();
            String deskripsiBaru = ((TextView) view.findViewById(R.id.deskripsiText)).getText().toString();
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentShop.getTokoid())));
            parameter.add(new BasicNameValuePair(Toko.TAG_NAMATOKO, namaBaru));
            parameter.add(new BasicNameValuePair(Toko.TAG_DESKRIPSITOKO, deskripsiBaru));

            String url = AppHelper.domainURL + "/AndroidConnect/PutTokoNameDeskripsi.php";
            UpdateDataTask task = new UpdateDataTask(url,parameter,getActivity());
            task.execute();
        }

        //UpdateDataTask task = new UpdateDataTask()
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

    ImageView shopImageView;
    private void initializeDetailShop(View view)
    {
        activeEdit=false;
        Intent i = getActivity().getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop!=null) {
            shopImageView = (ImageView) view.findViewById(R.id.shopImageView);
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



    private int REQUEST_CAMERA=0;
    private int SELECT_FILE=1;
    String userChoosenTask="";
    String url = AppHelper.domainURL + "/AndroidConnect/PutGambarToko.php";
    Bitmap gbr;

    public void changePicture(View view)
    {
        selectImage();
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
        gbr = bm;
        uploadPict(destination.getPath());
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
        gbr = thumbnail;
        uploadPict(destination.getPath());
    }

    private class LastMethod implements IUploadFinalMethod
    {
        String path;
        LastMethod(String path)
        {
            this.path = path;
        }
        @Override
        public void finalMethod() {
            //currentShop.setGambartoko(path);

            if(gbr!=null) {
                shopImageView.setImageBitmap(gbr);
            }
        }
    }

    private void uploadPict(String path)
    {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if(currentShop != null) {
            param.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentShop.getTokoid())));
            UploadLoadTask task = new UploadLoadTask(url, Toko.TAG_GAMBARTOKO, path, param,getActivity(),new LastMethod(path));
            task.execute();
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
