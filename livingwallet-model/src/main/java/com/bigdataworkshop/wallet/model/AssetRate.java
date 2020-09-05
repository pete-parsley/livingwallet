package com.bigdataworkshop.wallet.model;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Measurement(name="asset_rate", database="livingwallet")
public class AssetRate {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rateDateFormatted;
    @Column(name="asset_class")
    private String assetClass;
    @Column(name="asset_name")
    private String assetName;
    @Column(name="rate")
    private double rate;

    public AssetRate(){

    }

    public AssetRate(LocalDate rateDateFormatted, String assetClass, String assetName, double rate) {
        this.rateDateFormatted = rateDateFormatted;
        this.assetClass = assetClass;
        this.assetName = assetName;
        this.rate = rate;
    }

    public LocalDate getRateDateFormatted() {
        return rateDateFormatted;
    }

    public void setRateDateFormatted(LocalDate rateDateFormatted) {
        this.rateDateFormatted = rateDateFormatted;
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
