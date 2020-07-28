package com.bigdataworkshop.wallet.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Asset {

    private float pricing;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pricingDate;
    private String assetShortName;
    private String assetLongName;
    private String assetDescription;
    private float numberUnits;

    public Asset(){

    }

    public Asset(float pricing, Date pricingDate, String assetShortName, String assetLongName, String assetDescription,float numberUnits) {
        this.pricing = pricing;
        this.pricingDate = pricingDate;
        this.assetShortName = assetShortName;
        this.assetLongName = assetLongName;
        this.assetDescription = assetDescription;
    }

    public float getPricing() {
        return pricing;
    }

    public void setPricing(float pricing) {
        this.pricing = pricing;
    }

    public Date getPricingDate() {
        return pricingDate;
    }

    public void setPricingDate(Date pricingDate) {
        this.pricingDate = pricingDate;
    }

    public String getAssetShortName() {
        return assetShortName;
    }

    public void setAssetShortName(String assetShortName) {
        this.assetShortName = assetShortName;
    }

    public String getAssetLongName() {
        return assetLongName;
    }

    public void setAssetLongName(String assetLongName) {
        this.assetLongName = assetLongName;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public float getNumberUnits() {
        return numberUnits;
    }

    public void setNumberUnits(float numberUnits) {
        this.numberUnits = numberUnits;
    }
}
