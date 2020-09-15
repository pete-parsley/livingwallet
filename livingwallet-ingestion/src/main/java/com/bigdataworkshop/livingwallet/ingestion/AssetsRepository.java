package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetClass;
import com.bigdataworkshop.wallet.model.AssetRate;
import com.bigdataworkshop.wallet.model.AssetTransaction;
import com.bigdataworkshop.wallet.model.TransactionType;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;
import org.influxdb.impl.InfluxDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AssetsRepository {

    private static InfluxDB influxDB;
    private static InfluxDBMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(AssetsRepository.class);

    public AssetsRepository() {
        influxDB = InfluxDBFactory.connect("http://localhost:8087");
        mapper = new InfluxDBMapper(influxDB);
        Pong response = influxDB.ping();
        influxDB.setDatabase("livingwallet");
        logger.info(response.getVersion());
    }

    public void saveAssetRate(AssetRate assetRate){

        String assetRateMesurement;

        switch (assetRate.getAssetClass()) {
            case "CURRENCY":
                assetRateMesurement = "currency_rates";
                break;

            case "METAL":
                assetRateMesurement = "metal_rates";
                break;

            default:
                assetRateMesurement = "";
                break;

        }

        Point point = Point.measurement(assetRateMesurement).time(assetRate.getPricingDate().toEpochMilli(), TimeUnit.MILLISECONDS)
                            .tag("currency", assetRate.getAssetName())
                            .addField("rate", assetRate.getRate())
                            .build();
        influxDB.write(point);

    }



    public void saveAssetTransaction(AssetTransaction assetTransaction, AssetClass assetClass){



        Point point = Point.measurement("asset_transactions").time(assetTransaction.getPricingDateFormatted().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.of("Z")), TimeUnit.SECONDS)
                .tag("short_name", assetTransaction.getAssetShortName())
                .tag("long_name", assetTransaction.getAssetLongName())
                .tag("transaction_type",assetTransaction.getTransactionType())
                .addField("description", assetTransaction.getAssetDescription())
                .addField("pricing", assetTransaction.getPricing())
                .addField("number_units", (assetTransaction.getTransactionType().equals("BUY")) ? assetTransaction.getNumberUnits() : (-1) * assetTransaction.getNumberUnits())
                .addField("asset_class", assetTransaction.getAssetClass())
                .build();
        influxDB.write(point);

//counter transaction - sell asset convert into PLN Currency asset
        if(assetTransaction.getTransactionType().equals("SELL")) {
            Point sellPoint = Point.measurement("asset_transactions").time(assetTransaction.getPricingDateFormatted().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.of("Z")), TimeUnit.SECONDS)
                    .tag("short_name", "PLN")
                    .tag("long_name", "cash from sell transaction")
                    .tag("transaction_type", TransactionType.BUY.toString())
                    .addField("description", "cash from sell transaction")
                    .addField("pricing", assetTransaction.getPricing())
                    .addField("number_units", assetTransaction.getNumberUnits())
                    .addField("asset_class", AssetClass.CURRENCY.toString())
                    .build();
            influxDB.write(sellPoint);
        }
    }


    public List<AssetTransaction> getAllAssetTransactions(){


        Query query = new Query("SELECT * FROM asset_transactions");
        List<AssetTransaction> assetTransactions = mapper.query(query, AssetTransaction.class);
        logger.info("Number of Assets retrieved: " + assetTransactions.size());
        return assetTransactions;

    }

    public List<AssetTransaction> getAssetTransactionsByClass(AssetClass assetClass){

        Query query;
        switch(assetClass) {
            case CURRENCY:
                query = new Query("SELECT * FROM asset_transactions WHERE asset_class='CURRENCY'");
                break;
            case METAL:
                query = new Query("SELECT * FROM asset_transactions WHERE asset_class='METAL'");
                break;
            default:
                query = null;
                break;
        }

        List<AssetTransaction> assetTransactions = mapper.query(query, AssetTransaction.class);
        logger.info("Number of Assets retrieved: " + assetTransactions.size());
        return assetTransactions;

    }



    public void removeAssetTransaction(String timestamp, String longName, String shortName, String transactionType, AssetClass assetClass){

        String measurementName;

        switch(assetClass) {
            case CURRENCY:
                measurementName = "asset_transactions";
                break;
            case METAL:
                measurementName = "asset_transactions";
                break;
            default:
                measurementName = "";
                break;
        }

        Query bindQuery = new BoundParameterQuery.QueryBuilder().newQuery("DELETE FROM " + measurementName + " WHERE time=$time AND long_name=$long_name AND short_name=$short_name AND transaction_type=$transaction_type")
                                                            .bind("time",timestamp)
                                                            .bind("long_name",longName)
                                                            .bind("short_name", shortName)
                                                            .bind("transaction_type", transactionType)
                                                            .create();

        logger.info(bindQuery.getCommand());

        QueryResult result = influxDB.query(bindQuery);
        logger.info(result.toString());

    }


}
