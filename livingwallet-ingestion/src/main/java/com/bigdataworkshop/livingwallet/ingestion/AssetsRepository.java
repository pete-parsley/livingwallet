package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Asset;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        Point point = Point.measurement("currency_rates").time(currencyAsset.getPricingDate().getTime(), TimeUnit.MILLISECONDS)
                            .addField("pair",currencyAsset.getAssetShortName())
                            .addField("rate",currencyAsset.getPricing())
                            .build();
        influxDB.write(point);

    }

    public void saveCurrencyAsset(Asset currencyAsset){
        Point point = Point.measurement("assets").time(currencyAsset.getPricingDate().getTime(), TimeUnit.MILLISECONDS)
                .addField("short_name", currencyAsset.getAssetShortName())
                .addField("long_name", currencyAsset.getAssetLongName())
                .addField("description", currencyAsset.getAssetDescription())
                .addField("pricing", currencyAsset.getPricing())
                .addField("number_units", currencyAsset.getNumberUnits())
                .addField("asset_class", currencyAsset.getAssetClass())
                .build();
        influxDB.write(point);
    }

    public List<Asset> getAllCurrencyAssets(){


        Query query = new Query("select * from currency_assets");
        List<Asset> assets = mapper.query(query,Asset.class);
        logger.info("Number of Assets retrieved: " + assets.size());
        return new ArrayList<>();

    }


}
