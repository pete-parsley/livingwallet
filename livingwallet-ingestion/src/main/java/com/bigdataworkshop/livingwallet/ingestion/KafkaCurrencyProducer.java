package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Asset;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaCurrencyProducer {

    private static final String CURRENCY_RATES = "currency_rates";
    private static final String CURRENCY_ASSETS = "currency_assets";

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(KafkaCurrencyProducer.class);

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

    public void sendCurrencyRateMessage(Asset currencyAsset) {

        JsonNode currencyAssetJson = objectMapper.valueToTree(currencyAsset);
        logger.info(String.format("$$ -> Produced Message -> %s", currencyAssetJson.toString()));
        kafkaTemplate.send(CURRENCY_RATES,currencyAssetJson.toString());
    }

    public void sendCurrencyAssetMessage(Asset currencyAsset) {

        JsonNode currencyAssetJson = objectMapper.valueToTree(currencyAsset);
        logger.info(String.format("$$ -> Produced Message -> %s", currencyAssetJson.toString()));
        kafkaTemplate.send(CURRENCY_ASSETS,currencyAssetJson.toString());
    }


}
