package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetTransaction;
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
        influxDB = InfluxDBFactory.connect("http://localhost:8086");
        mapper = new InfluxDBMapper(influxDB);
        Pong response = influxDB.ping();
        influxDB.setDatabase("livingwallet");
        logger.info(response.getVersion());
    }

    public void saveCurrencyRate(AssetTransaction currencyAssetTransaction){
        Point point = Point.measurement("currency_rates").time(currencyAssetTransaction.getPricingDate().toEpochMilli(), TimeUnit.MILLISECONDS)
                            .addField("currency", currencyAssetTransaction.getAssetShortName())
                            .addField("rate", currencyAssetTransaction.getPricing())
                            .build();
        influxDB.write(point);

    }

    public void saveCurrencyTransaction(AssetTransaction currencyAssetTransaction){
        Point point = Point.measurement("currency_asset_transactions").time(currencyAssetTransaction.getPricingDateFormatted().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.of("Z")), TimeUnit.SECONDS)
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

    public List<AssetTransaction> getAllCurrencyAssetTransactions(){


        Query query = new Query("SELECT * FROM currency_asset_transactions");
        List<AssetTransaction> assetTransactions = mapper.query(query, AssetTransaction.class);
        logger.info("Number of Assets retrieved: " + assetTransactions.size());
        return assetTransactions;

    }

    public void removeCurrencyAsset(String timestamp, String longName, String shortName, String transactionType){

        Query bindQuery = new BoundParameterQuery.QueryBuilder().newQuery("DELETE FROM currency_asset_transactions WHERE time=$time AND long_name=$long_name AND short_name=$short_name AND transaction_type=$transaction_type")
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
