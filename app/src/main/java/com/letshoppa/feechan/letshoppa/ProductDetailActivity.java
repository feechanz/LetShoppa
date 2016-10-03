package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Produk;

public class ProductDetailActivity extends AppCompatActivity {

    Produk currentProduk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeProduct();
    }

    EditText nameEditText;
    EditText deskripsiEditText;
    EditText priceEditText;
    Spinner kategoriProdukSpinner;

    ImageButton editDescriptionButton;
    ImageButton editPriceButton;
    ImageButton editCategoryButton;

    ImageView productImageView;
    Button changePictureButton;

    private void initializeProduct()
    {
        Intent i = this.getIntent();
        currentProduk = (Produk) i.getSerializableExtra(Produk.TAG_PRODUK);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        deskripsiEditText = (EditText) findViewById(R.id.deskripsiText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        kategoriProdukSpinner = (Spinner) findViewById(R.id.kategoriproductspinner);

        editDescriptionButton = (ImageButton) findViewById(R.id.editImageBtn);
        editPriceButton = (ImageButton) findViewById(R.id.editPriceImageBtn);
        editCategoryButton = (ImageButton) findViewById(R.id.editCategoryImageBtn);

        productImageView = (ImageView) findViewById(R.id.productImageView);
        changePictureButton = (Button) findViewById(R.id.changePictureButton);

        activeEditDescription = false;
        activeEditPrice = false;
        activeEditCategory = false;
        kategoriProdukSpinner.setEnabled(false);

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
        setDefaultValue();
    }

    private void setDefaultValue()
    {
        defaultPicture();
        defaultDescription();
        defaultPrice();
        defaultCategory();
    }

    Boolean activeEditDescription;
    Boolean activeEditPrice;
    Boolean activeEditCategory;
    private void changePicture()
    {
        //change picture
    }

    private void editDescription()
    {
        activeEditDescription = !activeEditDescription;
        TextView editTextView = (TextView) findViewById(R.id.editTextView);
        ImageButton editImageButotn = editDescriptionButton;
        nameEditText.setEnabled(activeEditDescription);
        deskripsiEditText.setEnabled(activeEditDescription);
        if(activeEditDescription)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            editTextView.setText(getString(R.string.Edit));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }

    private void editPrice()
    {
        activeEditPrice = !activeEditPrice;
        TextView editTextView = (TextView) findViewById(R.id.editPriceTextView);
        ImageButton editImageButotn = editPriceButton;
        priceEditText.setEnabled(activeEditPrice);
        if (activeEditPrice)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            editTextView.setText(getString(R.string.Edit));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_edit);
        }
    }

    private void editCategory()
    {
        activeEditCategory = !activeEditCategory;
        TextView editTextView = (TextView) findViewById(R.id.editCategoryTextView);
        ImageButton editImageButotn = editCategoryButton;
        kategoriProdukSpinner.setEnabled(activeEditCategory);
        if(activeEditCategory)
        {
            editTextView.setText(getString(R.string.Save));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_save);
        }
        else
        {
            //save data
            editTextView.setText(getString(R.string.Edit));
            editImageButotn.setImageResource(android.R.drawable.ic_menu_edit);
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
        }
    }
}
