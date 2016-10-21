package com.letshoppa.feechan.letshoppa;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.JenistokoAdapter;
import com.letshoppa.feechan.letshoppa.AdapterList.KategoriprodukItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Jenistoko;
import com.letshoppa.feechan.letshoppa.Class.Kategoriproduk;
import com.letshoppa.feechan.letshoppa.Class.Produk;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Class.UploadLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Utility;
import com.letshoppa.feechan.letshoppa.Interface.IUploadFinalMethod;

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

public class MyProductDetailActivity extends AppCompatActivity {

    Produk currentProduk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_detail);

        initializeProduct();
    }

    EditText nameEditText;
    EditText deskripsiEditText;
    EditText priceEditText;
    Spinner kategoriProdukSpinner;
    Spinner statusProdukSpinner;

    ImageButton editDescriptionButton;
    ImageButton editPriceButton;
    ImageButton editCategoryButton;
    ImageButton editStatusButton;

    ImageView productImageView;
    Button changePictureButton;

    int jenistokoid;

    private void initializeProduct()
    {
        Intent i = this.getIntent();
        currentProduk = (Produk) i.getSerializableExtra(Produk.TAG_PRODUK);
        jenistokoid = i.getIntExtra(Toko.TAG_JENISTOKOID,0);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        deskripsiEditText = (EditText) findViewById(R.id.deskripsiText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        kategoriProdukSpinner = (Spinner) findViewById(R.id.kategoriproductspinner);
        statusProdukSpinner = (Spinner) findViewById(R.id.statusprodukspinner);

        editDescriptionButton = (ImageButton) findViewById(R.id.editImageBtn);
        editPriceButton = (ImageButton) findViewById(R.id.editPriceImageBtn);
        editCategoryButton = (ImageButton) findViewById(R.id.editCategoryImageBtn);
        editStatusButton = (ImageButton) findViewById(R.id.editStatusImageBtn);

        productImageView = (ImageView) findViewById(R.id.productImageView);
        changePictureButton = (Button) findViewById(R.id.changePictureButton);

        activeEditDescription = false;
        activeEditPrice = false;
        activeEditCategory = false;
        activeEditStatus = false;
        kategoriProdukSpinner.setEnabled(false);
        statusProdukSpinner.setEnabled(false);

        setButtonEvent();
    }
    private void setButtonEvent()
    {
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });
        editDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription();
            }
        });
        editPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPrice();
            }
        });
        editCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCategory();
            }
        });
        editStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editStatus();
            }
        });
        setDefaultValue();
    }

    private void setDefaultValue()
    {
        defaultPicture();
        defaultDescription();
        defaultPrice();
        defaultCategory();
        defaultStatus();
    }

    Boolean activeEditDescription;
    Boolean activeEditPrice;
    Boolean activeEditCategory;
    Boolean activeEditStatus;


    private void editDescription()
    {
        activeEditDescription = !activeEditDescription;
        TextView editTextView = (TextView) findViewById(R.id.editTextView);
        ImageButton editImageButton = editDescriptionButton;
        nameEditText.setEnabled(activeEditDescription);
        deskripsiEditText.setEnabled(activeEditDescription);
        if(activeEditDescription)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButton.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            saveDescription();
            editTextView.setText(getString(R.string.Edit));
            editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }



    private void editPrice()
    {
        activeEditPrice = !activeEditPrice;
        TextView editTextView = (TextView) findViewById(R.id.editPriceTextView);
        ImageButton editImageButton = editPriceButton;
        priceEditText.setEnabled(activeEditPrice);
        if (activeEditPrice)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButton.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            savePrice();
            editTextView.setText(getString(R.string.Edit));
            editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }

    private void editCategory()
    {
        activeEditCategory = !activeEditCategory;
        TextView editTextView = (TextView) findViewById(R.id.editCategoryTextView);
        ImageButton editImageButton = editCategoryButton;
        kategoriProdukSpinner.setEnabled(activeEditCategory);
        if(activeEditCategory)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButton.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            editTextView.setText(getString(R.string.Edit));
            editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }
    private void editStatus()
    {
        activeEditStatus = !activeEditStatus;
        TextView editTextView = (TextView) findViewById(R.id.editStatusTextView);
        ImageButton editImageButton = editStatusButton;
        statusProdukSpinner.setEnabled(activeEditStatus);
        if(activeEditStatus)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButton.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            editTextView.setText(getString(R.string.Edit));
            editImageButton.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }


    private void defaultPicture()
    {
        if(currentProduk!=null)
        {
            ImageLoadTask imageTask = new ImageLoadTask(currentProduk.getGambarproduk(),productImageView);
            imageTask.execute();
        }
    }
    private void defaultDescription()
    {
        if(currentProduk!=null) {
            setTitle(currentProduk.getNamaproduk());
            nameEditText.setText(currentProduk.getNamaproduk());
            deskripsiEditText.setText(currentProduk.getDeskripsiproduk());
        }
    }
    private void defaultPrice()
    {
        if(currentProduk != null)
        {
            priceEditText.setText(String.valueOf(currentProduk.getHargaproduk()));
        }
    }

    private void defaultCategory()
    {
        if(currentProduk != null)
        {
            //set spinner category product
            List listkategori = new ArrayList();
            ArrayAdapter mAdapter = new KategoriprodukItemAdapter(MyProductDetailActivity.this, listkategori);
            kategoriProdukSpinner.setAdapter(mAdapter);
            EditLoadKategoriTask kategoriTask = new EditLoadKategoriTask(this,jenistokoid,mAdapter);
            kategoriTask.execute();
        }
    }

    private void defaultStatus()
    {
        if(currentProduk != null)
        {
            //set spinner status product
            List listJenisToko = new ArrayList();
            listJenisToko.add(new Jenistoko(1,getString(R.string.dijual)));
            listJenisToko.add(new Jenistoko(2,getString(R.string.habis)));
            listJenisToko.add(new Jenistoko(3,getString(R.string.tidak_dijual)));

            final ArrayAdapter mAdapter = new JenistokoAdapter(MyProductDetailActivity.this, listJenisToko);
            statusProdukSpinner.setAdapter(mAdapter);

            if(currentProduk.getStatusproduk() == 1)
            {
                //dijual
                statusProdukSpinner.setSelection(0);
            }
            else if(currentProduk.getStatusproduk() == 2)
            {
                //habis
                statusProdukSpinner.setSelection(1);
            }
            else
            {
                //tidak dijual
                statusProdukSpinner.setSelection(2);
            }
        }
    }

    private void saveDescription()
    {
        String newname = nameEditText.getText().toString();
        String newdescription = deskripsiEditText.getText().toString();
        String url = AppHelper.domainURL + "/AndroidConnect/PutProdukNameDeskripsi.php";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if(currentProduk != null) {
            parameter.add(new BasicNameValuePair(Produk.TAG_PRODUKID, String.valueOf(currentProduk.getProdukid())));
            parameter.add(new BasicNameValuePair(Produk.TAG_NAMAPRODUK, newname));
            parameter.add(new BasicNameValuePair(Produk.TAG_DESKRIPSIPRODUK, newdescription));

            UpdateDataTask task = new UpdateDataTask(url,parameter,MyProductDetailActivity.this);
            task.execute();
        }
    }

    private void savePrice()
    {
        String newprice = priceEditText.getText().toString();
        String url = AppHelper.domainURL + "/AndroidConnect/PutProdukPrice.php";
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        if(currentProduk != null) {
            parameter.add(new BasicNameValuePair(Produk.TAG_PRODUKID, String.valueOf(currentProduk.getProdukid())));
            parameter.add(new BasicNameValuePair(Produk.TAG_HARGAPRODUK, newprice));

            UpdateDataTask task = new UpdateDataTask(url,parameter,MyProductDetailActivity.this);
            task.execute();
        }
    }

    private int REQUEST_CAMERA=0;
    private int SELECT_FILE=1;
    String userChoosenTask="";
    String url = AppHelper.domainURL + "/AndroidConnect/PutGambarProduk.php";
    Bitmap gbr;

    ///upload picture
    private void changePicture()
    {
        //change picture
        selectImage();
    }
    private void selectImage() {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_Library),
                getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProductDetailActivity.this);
        builder.setTitle(getString(R.string.change_picture));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(MyProductDetailActivity.this);
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
                bm = MediaStore.Images.Media.getBitmap(MyProductDetailActivity.this.getApplicationContext().getContentResolver(), data.getData());
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
    private class LastMethod implements IUploadFinalMethod {


        LastMethod() {

        }

        @Override
        public void finalMethod() {
            //currentShop.setGambartoko(path);

            if (gbr != null) {
                productImageView.setImageBitmap(gbr);
            }
        }
    }
    private void uploadPict(String path)
    {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if(currentProduk != null) {
            param.add(new BasicNameValuePair(Produk.TAG_PRODUKID, String.valueOf(currentProduk.getProdukid())));
            UploadLoadTask task = new UploadLoadTask(url, Produk.TAG_GAMBARPRODUK, path, param,MyProductDetailActivity.this,new LastMethod());
            task.execute();
        }
        else
        {
            Toast.makeText(MyProductDetailActivity.this, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public class EditLoadKategoriTask extends AsyncTask<Void, Void, Boolean> {
        Activity activity;
        int jenistokoid;
        ArrayAdapter mAdapter;
        String messagejson;
        int successjson;
        Dialog dialog;
        String url = AppHelper.domainURL+"/AndroidConnect/GetAllKategoriProdukByJenistokoid.php";

        JSONArray mykategoris = null;
        List kategoris;

        public EditLoadKategoriTask(Activity activity, int jenistokoid, ArrayAdapter mAdapter) {
            this.activity = activity;
            this.jenistokoid = jenistokoid;
            this.mAdapter = mAdapter;

            dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            kategoris = new ArrayList();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Toko.TAG_JENISTOKOID, String.valueOf(jenistokoid)));
            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json != null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        mykategoris = json.getJSONArray(Kategoriproduk.TAG_KATEGORIPRODUK);

                        for (int i = 0; i < mykategoris.length(); i++) {
                            JSONObject kategoriobject = mykategoris.getJSONObject(i);
                            Kategoriproduk kategori = Kategoriproduk.GetKategoriprodukFromJson(kategoriobject);
                            kategoris.add(kategori);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    messagejson = e.getMessage();
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
                mAdapter.clear();
                mAdapter.addAll(kategoris);
                int idx =0;
                for(int i =0;i<kategoris.size();i++)
                {
                    if( ((Kategoriproduk) kategoris.get(i)).getKategoriprodukid() == currentProduk.getKategoriprodukid()) {
                        idx = i;
                        break;
                    }
                }
                kategoriProdukSpinner.setSelection(idx);
            }
            else
            {
                Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

}
