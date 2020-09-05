package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetClass;
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

    public void saveAssetRate(AssetTransaction currencyAssetTransaction, AssetClass assetClass){
        Point point = Point.measurement("currency_rates").time(currencyAssetTransaction.getPricingDate().toEpochMilli(), TimeUnit.MILLISECONDS)
                            .tag("currency", currencyAssetTransaction.getAssetShortName())
                            .addField("rate", currencyAssetTransaction.getPricing())
                            .build();
        influxDB.write(point);

    }



    public void saveAssetTransaction(AssetTransaction currencyAssetTransaction, AssetClass assetClass){

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



        Point point = Point.measurement(measurementName).time(currencyAssetTransaction.getPricingDateFormatted().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.of("Z")), TimeUnit.SECONDS)
                .tag("short_name", currencyAssetTransaction.getAssetShortName())
                .tag("long_name", currencyAssetTransaction.getAssetLongName())
                .tag("transaction_type",currencyAssetTransaction.getTransactionType())
                .addField("description", currencyAssetTransaction.getAssetDescription())
                .addField("pricing", currencyAssetTransaction.getPricing())
                .addField("number_units", (currencyAssetTransaction.getTransactionType().equals("BUY")) ? currencyAssetTransaction.getNumberUnits() : (-1) * currencyAssetTransaction.getNumberUnits())
                .addField("asset_class", currencyAssetTransaction.getAssetClass())
                .build();
        influxDB.write(point);
    }


    public List<AssetTransaction> getAllAssetTransactions(AssetClass assetClass){

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
