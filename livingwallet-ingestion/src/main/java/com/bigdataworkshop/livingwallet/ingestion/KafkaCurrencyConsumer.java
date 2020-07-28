package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.CurrencyAsset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaCurrencyConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaCurrencyConsumer.class);
    @Autowired
    DBWriter dbWriter;

    @KafkaListener(topics = "currency_rates", groupId = "group_id")
    public void consume(String message) throws JsonProcessingException {
        logger.info(String.format("$$ -> Consumed Message -> %s", message));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currencyRateJson = objectMapper.readTree(message);
        CurrencyAsset currencyAsset = objectMapper.treeToValue(currencyRateJson,CurrencyAsset.class);
        logger.info(String.format("$$ -> CurrencyAsset:  -> %s,%s,%s", currencyAsset.getCurrencyPair(),currencyAsset.getPricing(),currencyAsset.getPricingDate()));
        dbWriter.storeCurrencyRate(currencyAsset);
    }
}
