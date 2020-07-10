package com.bigdataworkshop.wallet.model;

import java.util.Date;

public class Asset {

    private float pricing;
    private Date pricingDate;

    public Asset(float pricing, Date pricingDate) {
        this.pricing = pricing;
        this.pricingDate = pricingDate;
    }

    public float getPricing() {
        return pricing;
    }

    public void setPricing(float pricing) {
        this.pricing = pricing;
    }
}
