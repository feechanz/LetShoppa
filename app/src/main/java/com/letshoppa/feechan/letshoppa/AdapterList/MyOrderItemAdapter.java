package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.ChangeOrderTask;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/20/2016.
 */

public class MyOrderItemAdapter extends ArrayAdapter {
    private Context context;
    private IRefreshMethod refreshMethod;

    public MyOrderItemAdapter(Context context, List items, IRefreshMethod refreshMethod) {
        super(context, -1, items);
        this.context = context;
        this.refreshMethod = refreshMethod;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Order item = (Order) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_my_order_item,parent,false);

        TextView nameTextView = (TextView) viewToUse.findViewById(R.id.nameTextView);
        TextView shopNameTextView = (TextView) viewToUse.findViewById(R.id.shopNameTextView);

        TextView nameProductTextView = (TextView) viewToUse.findViewById(R.id.namaProductTextView);
        TextView priceTextView = (TextView) viewToUse.findViewById(R.id.priceTextView);
        TextView qtyTextView = (TextView) viewToUse.findViewById(R.id.qtyTextView);
        TextView totalTextView = (TextView) viewToUse.findViewById(R.id.totalTextView);
        TextView dateTextView = (TextView) viewToUse.findViewById(R.id.dateTextView);

        ImageView iconProductImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_product);

        if(item != null) {
            nameTextView.setText(item.getNamapembeli());
            shopNameTextView.setText(item.getNamatoko());

            nameProductTextView.setText(item.getNamaproduk());
            priceTextView.setText(String.valueOf(item.getHargaproduk()));
            qtyTextView.setText(String.valueOf(item.getJumlahproduk()));
            double total = item.getJumlahproduk() * item.getHargaproduk();
            totalTextView.setText(String.valueOf(total));
            dateTextView.setText(item.getTanggalorder().toString());

            ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambarproduk(), iconProductImageView);
            loadShopTask.execute();
            if(item.getStatusorder()!=2)
            {
                initializePurchasedOrder(viewToUse,item);
            }
            else
            {
                initializeMyOrder(viewToUse,item);
            }
            initializeStatus(viewToUse,item);
        }

        return viewToUse;
    }

    public void initializeStatus(View view,Order pesanan)
    {
        TextView statusTextView = (TextView) view.findViewById(R.id.statusTextView);
        if(pesanan.getStatusorder() == 3)
        {
            //accepted
            statusTextView.setText(context.getString(R.string.accepted));
        }
        else if(pesanan.getStatusorder() == 0)
        {
            //cancelled
            statusTextView.setText(context.getString(R.string.cancelled));
        }
        else if(pesanan.getStatusorder() == 1)
        {
            //wantobuy
            statusTextView.setText(context.getString(R.string.wanttobuy));
        }
    }
    public void initializePurchasedOrder(View view, Order pesanan)
    {
        Button firstBtn = (Button) view.findViewById(R.id.firstButton);
        Button secondBtn = (Button) view.findViewById(R.id.secondButton);
        firstBtn.setVisibility(View.GONE);
        secondBtn.setVisibility(View.GONE);
    }

    public void initializeMyOrder(View view, final Order pesanan)
    {
        Button firstBtn = (Button) view.findViewById(R.id.firstButton);
        Button secondBtn = (Button) view.findViewById(R.id.secondButton);

        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderDialog(pesanan,3,R.string.prompt_accept_order);
            }
        });

        secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderDialog(pesanan,0,R.string.prompt_reject_order);
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
                        //update
                        ChangeOrderTask task = new ChangeOrderTask(pesanan.getOrderid(),newstatus,context,refreshMethod);
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
}