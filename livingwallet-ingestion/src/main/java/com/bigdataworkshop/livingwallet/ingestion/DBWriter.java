package com.bigdataworkshop.livingwallet.ingestion;

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

    public void storeDataPoint(float rate){
        Point point = Point.measurement("currency_rates").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                            .addField("pair","usd->pln")
                            .addField("rate",rate)
                            .build();
        influxDB.write(point);

    }


}
