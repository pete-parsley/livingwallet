package com.bigdataworkshop.wallet.model;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Measurement(name="asset_transactions", database="livingwallet")
public class AssetTransaction {

    @Column(name="pricing")
    private double pricing;
    @Column(name="time")
    private Instant pricingDate;
    @Column(name="short_name")
    private String assetShortName;
    @Column(name="long_name")
    private String assetLongName;
    @Column(name="description")
    private String assetDescription;
    @Column(name="number_units")
    private double numberUnits;
    @Column(name="asset_class")
    private String assetClass;
    @Column(name="transaction_type")
    private String transactionType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pricingDateFormatted;

    public AssetTransaction(){

    }

    public AssetTransaction(double pricing, Instant pricingDate, String assetShortName, String assetLongName, String assetDescription, double numberUnits, String assetClass, LocalDate pricingDateFormatted, String transactionType) {
        this.pricing = pricing;
        this.pricingDate = pricingDate;
        this.assetShortName = assetShortName;
        this.assetLongName = assetLongName;
        this.assetDescription = assetDescription;
        this.numberUnits = numberUnits;
        this.assetClass = assetClass;
        this.pricingDateFormatted = pricingDateFormatted;
        this.transactionType = transactionType;
    }

    public double getPricing() {
        return pricing;
    }

    public void setPricing(double pricing) {
        this.pricing = pricing;
    }

    public Instant getPricingDate() {
        return pricingDate;
    }

    public void setPricingDate(Instant pricingDate) {
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

    public double getNumberUnits() {
        return numberUnits;
    }

    public void setNumberUnits(double numberUnits) {
        this.numberUnits = numberUnits;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public LocalDate getPricingDateFormatted() {
        return pricingDateFormatted;
    }

    public void setPricingDateFormatted(LocalDate pricingDateFormatted) {
        this.pricingDateFormatted = pricingDateFormatted;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

}
