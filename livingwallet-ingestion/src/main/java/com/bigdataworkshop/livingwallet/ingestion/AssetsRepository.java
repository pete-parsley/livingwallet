package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Asset;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.*;
import org.influxdb.impl.InfluxDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
        System.out.println(response.getVersion());
    }

    public void saveCurrencyRate(Asset currencyAsset){
        Point point = Point.measurement("currency_rates").time(currencyAsset.getPricingDate().toEpochMilli(), TimeUnit.MILLISECONDS)
                            .addField("currency",currencyAsset.getAssetShortName())
                            .addField("rate",currencyAsset.getPricing())
                            .build();
        influxDB.write(point);

    }

    public void saveCurrencyAssetBuy(Asset currencyAsset){
        Point point = Point.measurement("currency_asset_transactions").time(currencyAsset.getPricingDateFormatted().toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.of("Z")), TimeUnit.SECONDS)
                .tag("short_name", currencyAsset.getAssetShortName())
                .tag("long_name", currencyAsset.getAssetLongName())
                .addField("description", currencyAsset.getAssetDescription())
                .addField("pricing", currencyAsset.getPricing())
                .addField("number_units", currencyAsset.getNumberUnits())
                .addField("a sset_class", currencyAsset.getAssetClass())
                .addField("transaction_type","BUY").build();
        influxDB.write(point);
    }

    public List<Asset> getAllCurrencyAssetTransactions(){


        Query query = new Query("select * from currency_asset_transactions");
        List<Asset> assets = mapper.query(query,Asset.class);
        logger.info("Number of Assets retrieved: " + assets.size());
        return assets;

    }

    public void removeCurrencyAsset(String timestamp, String longName, String shortName){

        Query bindQuery = new BoundParameterQuery.QueryBuilder().newQuery("DELETE FROM assets WHERE time=$time AND long_name=$long_name AND short_name=$short_name")
                                                            .bind("time",timestamp)
                                                            .bind("long_name",longName)
                                                            .bind("short_name", shortName)
                                                            .create();

        logger.info(bindQuery.getCommand());

        QueryResult result = influxDB.query(bindQuery);
        logger.info(result.toString());

    }


}
