package com.bigdataworkshop.wallet.model;

import java.util.Date;

public class CurrencyAsset extends Asset {

    private String currencySymbol;


    public CurrencyAsset(float pricing, Date pricingDate, String currencySymbol) {
        super(pricing, pricingDate);
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
