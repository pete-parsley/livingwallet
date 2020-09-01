package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaAssetConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaAssetConsumer.class);
    @Autowired
    AssetsRepository assetsRepository;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

    @KafkaListener(topics = "currency_rates", groupId = "group_id")
    public void consumeCurrencyRates(String message) throws JsonProcessingException {
        logger.info(String.format("$$ -> Consumed Message -> %s", message));
     //   ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currencyRateJson = objectMapper.readTree(message);
        AssetTransaction currencyAssetTransaction = objectMapper.treeToValue(currencyRateJson, AssetTransaction.class);
        logger.info(String.format("$$ -> Currency Rate:  -> %s,%s,%s", currencyAssetTransaction.getAssetShortName(), currencyAssetTransaction.getPricing(), currencyAssetTransaction.getPricingDate()));
        assetsRepository.saveCurrencyRate(currencyAssetTransaction);
    }

    @KafkaListener(topics = "currency_assets", groupId = "group_id")
    public void consumeCurrencyAssets(String message) throws JsonProcessingException {
        logger.info(String.format("$$ -> Consumed Message -> %s", message));
    //    ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currencyAssetJson = objectMapper.readTree(message);
        AssetTransaction currencyAssetTransaction = objectMapper.treeToValue(currencyAssetJson, AssetTransaction.class);
        logger.info(String.format("$$ -> Currency Asset:  -> %s,%s,%s,%f,%tD", currencyAssetTransaction.getAssetShortName(), currencyAssetTransaction.getAssetLongName(), currencyAssetTransaction.getAssetDescription(), currencyAssetTransaction.getPricing(), currencyAssetTransaction.getPricingDate()));
        assetsRepository.saveAssetTransaction(currencyAssetTransaction);
    }
}
