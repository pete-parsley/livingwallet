package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetClass;
import com.bigdataworkshop.wallet.model.AssetTransaction;
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
public class KafkaAssetProducer {

    private static final String CURRENCY_RATES = "currency_rates";
    private static final String CURRENCY_ASSETS = "currency_assets";
    private static final String METALS_RATES = "metals_rates";
    private static final String METALS_ASSETS = "metals_assets";

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(KafkaAssetProducer.class);

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

    public void sendAssetRateMessage(AssetTransaction assetTransaction) {

        JsonNode assetJson = objectMapper.valueToTree(assetTransaction);
        logger.info(String.format("$$ -> Produced Message -> %s", assetJson.toString()));
        switch (assetTransaction.getAssetClass()) {
            case "CURRENCY":
                kafkaTemplate.send(CURRENCY_RATES, assetJson.toString());
                break;
            case "METAL":
                kafkaTemplate.send(METALS_RATES, assetJson.toString());
            default:
                break;
        }
    }

    public void sendAssetTransactionMessage(AssetTransaction assetTransaction) {

        JsonNode assetJson = objectMapper.valueToTree(assetTransaction);
        logger.info(String.format("$$ -> Produced Message -> %s", assetJson.toString()));

        switch(assetTransaction.getAssetClass()){

            case "CURRENCY":
                kafkaTemplate.send(CURRENCY_ASSETS,assetJson.toString());
                break;
            case "METAL":
                kafkaTemplate.send(METALS_ASSETS,assetJson.toString());
                break;
            default:
                break;
        }

    }


}
