package com.letshoppa.feechan.letshoppa.Class;

import java.util.ArrayList;

/**
 * Created by Feechan on 10/9/2016.
 */

public class HeaderInfo {
    private String name;
    private ArrayList<Account> productList = new ArrayList<Account>();;

    public HeaderInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Account> getProductList() {
        return productList;
    }
    public void setProductList(ArrayList<Account> productList) {
        this.productList = productList;
    }
}
