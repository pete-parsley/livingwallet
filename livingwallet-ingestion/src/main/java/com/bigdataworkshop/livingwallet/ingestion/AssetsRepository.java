package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.CurrencyAsset;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DBWriter {

    private static InfluxDB influxDB;

    public DBWriter() {
        influxDB = InfluxDBFactory.connect("http://localhost:8086");
        Pong response = influxDB.ping();
        influxDB.setDatabase("livingwallet");
        System.out.println(response.getVersion());
    }

    public void saveCurrencyRate(CurrencyAsset currencyAsset){
        Point point = Point.measurement("currency_rates").time(currencyAsset.getPricingDate().getTime(), TimeUnit.MILLISECONDS)
                            .addField("pair",currencyAsset.getCurrencyPair())
                            .addField("rate",currencyAsset.getPricing())
                            .build();
        influxDB.write(point);

    }

    public void saveCurrencyAsset(CurrencyAsset currencyAsset){
        Point point = Point.measurement("currency_assets").time(currencyAsset.getPricingDate().getTime(), TimeUnit.MILLISECONDS)
                .addField("short_name", currencyAsset.getAssetShortName())
                .addField("long_name", currencyAsset.getAssetLongName())
                .addField("description", currencyAsset.getAssetDescription())
                .addField("pricing", currencyAsset.getPricing())
                .addField("number_units", currencyAsset.getNumberUnits())
                .build();
        influxDB.write(point);
    }


}
