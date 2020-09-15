package com.bigdataworkshop.wallet.model;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name="asset_rate", database="livingwallet")
public class AssetRate {

    @Column(name="time")
    private Instant pricingDate;
    @Column(name="asset_class")
    private String assetClass;
    @Column(name="asset_name")
    private String assetName;
    @Column(name="rate")
    private double rate;

    public AssetRate(){

    }

    public AssetRate(Instant pricingDate, String assetClass, String assetName, double rate) {
        this.pricingDate = pricingDate;
        this.assetClass = assetClass;
        this.assetName = assetName;
        this.rate = rate;
    }

    public Instant getPricingDate() {
        return pricingDate;
    }

    public void setPricingDate(Instant pricingDate) {
        this.pricingDate = pricingDate;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
