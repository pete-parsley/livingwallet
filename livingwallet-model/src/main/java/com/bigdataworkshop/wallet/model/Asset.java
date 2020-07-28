package com.bigdataworkshop.wallet.model;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Measurement(name="assets", database="livingwallet")
public class Asset {

    @Column(name="pricing")
    private float pricing;
    @Column(name="pricing_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pricingDate;
    @Column(name="short_name")
    private String assetShortName;
    @Column(name="long_name")
    private String assetLongName;
    @Column(name="description")
    private String assetDescription;
    @Column(name="number_units")
    private float numberUnits;
    @Column(name="asset_class")
    private String assetClass;

    public Asset(){

    }

    public Asset(float pricing, Date pricingDate, String assetShortName, String assetLongName, String assetDescription, float numberUnits, String assetClass) {
        this.pricing = pricing;
        this.pricingDate = pricingDate;
        this.assetShortName = assetShortName;
        this.assetLongName = assetLongName;
        this.assetDescription = assetDescription;
        this.numberUnits = numberUnits;
        this.assetClass = assetClass;
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

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }
}
