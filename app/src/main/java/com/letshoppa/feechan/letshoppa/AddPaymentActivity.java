package com.letshoppa.feechan.letshoppa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Class.Pembayaran;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Class.UploadLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Utility;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.Interface.IUploadFinalMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPaymentActivity extends AppCompatActivity {

    Order currentOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        setTitle(getString(R.string.add_payment));
        Intent i = getIntent();
        currentOrder = (Order) i.getSerializableExtra(Order.TAG_ORDER);
        initialize();
    }

    ImageView mPembayaranImageView;
    EditText mInformationEditText;
    EditText mCostEditText;

    private void initialize()
    {
        mPembayaranImageView = (ImageView) findViewById(R.id.pembayaranImageView);
        mInformationEditText = (EditText) findViewById(R.id.informationEditText);
        mCostEditText = (EditText) findViewById(R.id.costEditText);

        Button mCancelButton = (Button) findViewById(R.id.cancelButton);
        Button mChangeButton = (Button) findViewById(R.id.changeButton);
        Button mAddPaymentButton = (Button) findViewById(R.id.addPaymentButton);


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePicture();
            }
        });
        mAddPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPayment();
            }
        });
        path = "";
    }
    String path;
    private boolean validasiPayment()
    {
        boolean result = true;
        String biaya = mCostEditText.getText().toString();
        String information = mInformationEditText.getText().toString();
        View focusView = null;
        if(TextUtils.isEmpty(information))
        {
            mInformationEditText.setError(getString(R.string.error_field_required));
            focusView = mInformationEditText;
            result = false;
        }
        if(TextUtils.isEmpty(biaya))
        {
            mCostEditText.setError(getString(R.string.error_field_required));
            focusView = mCostEditText;
            result = false;
        }
        if(!result)
        {
            focusView.requestFocus();
        }
        return  result;
    }

    private void addPayment()
    {
        if(validasiPayment() && currentOrder != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/PostPembayaran.php";
            String biaya = mCostEditText.getText().toString();
            String information = mInformationEditText.getText().toString();

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Pembayaran.TAG_KETERANGAN, information));
            parameter.add(new BasicNameValuePair(Pembayaran.TAG_TOTALPEMBAYARAN, biaya));
            parameter.add(new BasicNameValuePair(Pembayaran.TAG_ORDERID, String.valueOf(currentOrder.getOrderid())));

            if(!TextUtils.isEmpty(path) && !path.equals("")) {
                UploadLoadTask task = new UploadLoadTask(url, Pembayaran.TAG_BUKTIPEMBAYARAN, path, parameter, AddPaymentActivity.this, new LastMethod());
                task.execute();
            }
            else
            {
                UpdateDataTask task = new UpdateDataTask(url,parameter,AddPaymentActivity.this,new LastMethod2());
                task.execute();
            }
        }
    }

    public class LastMethod2 implements IRefreshMethod
    {

        @Override
        public void refresh() {
            finish();
        }
    }
    public  class LastMethod  implements IUploadFinalMethod
    {

        @Override
        public void finalMethod() {
            finish();
        }
    }

    private int REQUEST_CAMERA=0;
    private int SELECT_FILE=1;
    String userChoosenTask="";
    String url = AppHelper.domainURL + "/AndroidConnect/PutGambarToko.php";
    Bitmap gbr;

    public void changePicture()
    {
        selectImage();
    }

    private void selectImage()
    {
        final CharSequence[] items = { getString(R.string.take_photo), getString(R.string.choose_from_Library),
                getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPaymentActivity.this);
        builder.setTitle(getString(R.string.payment_receipt));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddPaymentActivity.this);
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
                bm = MediaStore.Images.Media.getBitmap(AddPaymentActivity.this.getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bm = AppHelper.getResizedBitmap(bm,450,600);
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
        mPembayaranImageView.setImageBitmap(gbr);
        path = destination.getPath();
        //uploadPict(destination.getPath());
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = AppHelper.getResizedBitmap(thumbnail,450,600);

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
        mPembayaranImageView.setImageBitmap(gbr);
        path = destination.getPath();
        //uploadPict(destination.getPath());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
}
