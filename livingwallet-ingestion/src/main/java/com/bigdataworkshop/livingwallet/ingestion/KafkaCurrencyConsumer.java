package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Asset;
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
    AssetsRepository assetsRepository;

    @KafkaListener(topics = "currency_rates", groupId = "group_id")
    public void consumeCurrencyRates(String message) throws JsonProcessingException {
        logger.info(String.format("$$ -> Consumed Message -> %s", message));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currencyRateJson = objectMapper.readTree(message);
        Asset currencyAsset = objectMapper.treeToValue(currencyRateJson,Asset.class);
        logger.info(String.format("$$ -> Currency Rate:  -> %s,%s,%s", currencyAsset.getAssetShortName(),currencyAsset.getPricing(),currencyAsset.getPricingDate()));
        assetsRepository.saveCurrencyRate(currencyAsset);
    }

    @KafkaListener(topics = "currency_assets", groupId = "group_id")
    public void consumeCurrencyAssets(String message) throws JsonProcessingException {
        logger.info(String.format("$$ -> Consumed Message -> %s", message));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode currencyAssetJson = objectMapper.readTree(message);
        Asset currencyAsset = objectMapper.treeToValue(currencyAssetJson,Asset.class);
        logger.info(String.format("$$ -> Currency Asset:  -> %s,%s,%s,%f,%tD", currencyAsset.getAssetShortName(),currencyAsset.getAssetLongName(), currencyAsset.getAssetDescription(),currencyAsset.getPricing(),currencyAsset.getPricingDate()));
        assetsRepository.saveCurrencyAsset(currencyAsset);
    }
}
