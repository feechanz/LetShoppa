package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/18/2016.
 */

public class OrderItemAdapter extends ArrayAdapter
{
    private Context context;
    private IRefreshMethod refreshMethod;

    public OrderItemAdapter(Context context, List items,IRefreshMethod refreshMethod) {
        super(context, -1, items);
        this.context = context;
        this.refreshMethod = refreshMethod;

    }




    /*public void openShop(Toko toko)
    {
        Intent openShopIntent = new Intent(context,MyShopActivity.class);
        openShopIntent.putExtra(Toko.TAG_TOKO,toko);
        context.startActivity(openShopIntent);
    }*/
    public void initializeCartOrder(View view,final Order pesanan)
    {
        TextView statusTextView = (TextView) view.findViewById(R.id.statusTextView);
        Button firstBtn = (Button) view.findViewById(R.id.firstButton);
        Button secondBtn = (Button) view.findViewById(R.id.secondButton);
        statusTextView.setText(context.getString(R.string.wanttobuy));
        firstBtn.setText(context.getString(R.string.buy));
        secondBtn.setText(context.getString(R.string.cancel));

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderDialog(pesanan,2,R.string.prompt_buy_product);
            }
        });
        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrderDialog(pesanan);
            }
        });
    }
    private void updateOrderDialog(final Order pesanan, final int newstatus, int promptid)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        ChangeOrderTask task = new ChangeOrderTask(pesanan.getOrderid(),newstatus);
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(promptid)+"?")
                .setPositiveButton(context.getString(R.string.yes),dialogClickListener).
                setNegativeButton(context.getString(R.string.no),dialogClickListener).show();
    }
    private void deleteOrderDialog(Order pesanan)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //delete

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.prompt_cancel_order)+"?")
                .setPositiveButton(context.getString(R.string.yes),dialogClickListener).
                setNegativeButton(context.getString(R.string.no),dialogClickListener).show();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Order item = (Order) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_order_item,parent,false);

        TextView nameProductTextView = (TextView) viewToUse.findViewById(R.id.namaProductTextView);
        TextView priceTextView = (TextView) viewToUse.findViewById(R.id.priceTextView);
        TextView qtyTextView = (TextView) viewToUse.findViewById(R.id.qtyTextView);
        TextView totalTextView = (TextView) viewToUse.findViewById(R.id.totalTextView);


        ImageView iconProductImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_product);

        if(item != null) {
            nameProductTextView.setText(item.getNamaproduk());
            priceTextView.setText(String.valueOf(item.getHargaproduk()));
            qtyTextView.setText(String.valueOf(item.getJumlahproduk()));
            double total = item.getJumlahproduk() * item.getHargaproduk();
            totalTextView.setText(String.valueOf(total));
            ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambarproduk(), iconProductImageView);
            loadShopTask.execute();
            if(item.getStatusorder()==1)
            {
                initializeCartOrder(viewToUse,item);
            }
        }
        /*viewToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShop(item);
            }
        });*/
        //load shop image
//        ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambartoko(),iconShopImageView);
//        loadShopTask.execute();
//
//        namaKategoriTextView.setText(item.getNamajenis());
//        namaTokoTextView.setText(item.getNamatoko());
//        locationTextView.setText(context.getString(R.string.location)+" : "+ item.getLokasitoko());

        return viewToUse;
    }


    public class ChangeOrderTask extends AsyncTask<Void, Void, Boolean> {
        private String url = AppHelper.domainURL + "/AndroidConnect/PutStatusOrder.php";
        private int successjson;
        private String messagejson;

        int orderid;
        int statusorder;

        public ChangeOrderTask(int orderid, int statusorder) {
            messagejson="";
            successjson=-1;
            this.orderid = orderid;
            this.statusorder = statusorder;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Order.TAG_ORDERID, String.valueOf(orderid)));
            parameter.add(new BasicNameValuePair(Order.TAG_STATUSORDER, String.valueOf(statusorder)));

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        return true;
                    }
                    else
                    {
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
        protected void onPostExecute(final Boolean success)
        {
            if (success)
            {
                refreshMethod.refresh();
            }
            Toast.makeText(context, messagejson, Toast.LENGTH_SHORT).show();
            //set refresh off
        }
    }
}
