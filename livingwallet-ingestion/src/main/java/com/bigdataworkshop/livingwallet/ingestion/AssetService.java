package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class AssetService {

    Logger logger = LoggerFactory.getLogger(AssetService.class);

    @Autowired
    private AssetParser assetParser;


    @Autowired
    private KafkaAssetProducer kafkaCurrencyProducer;

    @Autowired
    private AssetsRepository assetsRepository;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;


    public AssetService() {

    }


    @Scheduled(fixedDelay = 10000)
    public void getRates(){
        if(!activeProfile.equals("dev")) {
            logger.info("Checking currency rates");
            List<AssetTransaction> requiredAssets = getRequiredAssetRates();
            for (AssetTransaction requiredAsset : requiredAssets) {
                logger.info("Getting rate for: " + requiredAsset.getAssetShortName());
                saveAssetRate(requiredAsset);
            }
        }
    }

    private List<AssetTransaction> getRequiredAssetRates(){
        List<AssetTransaction> requiredAssets = new ArrayList<>();

        List<AssetTransaction> assetTransactions = assetsRepository.getAllAssetTransactions();

        for(AssetTransaction assetTransaction : assetTransactions){
            if(!requiredAssets.contains(assetTransaction))
                requiredAssets.add(assetTransaction);
        }

        return requiredAssets;
    }




    public String saveAssetRate(AssetTransaction asset) {

        String rate = "0.0";

        switch(asset.getAssetClass()) {

            case "CURRENCY":
                rate = assetParser.getCurrencyRate(Currency.valueOf(asset.getAssetShortName()));
                break;
            case "METAL":
                rate = assetParser.getMetalRate(Metals.valueOf(asset.getAssetShortName()));
                break;
            default:
                break;

        }

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Instant dateobj = Instant.now();

        AssetRate assetRate = new AssetRate(dateobj,asset.getAssetClass(),asset.getAssetShortName(),Double.parseDouble(rate));
        //AssetTransaction currencyAssetTransaction = new AssetTransaction(Float.parseFloat(rate), dateobj, currency.toString(),"kurs wymiany walut","notowania",1.0f,"CURRENCY", LocalDate.now(),"");
        kafkaCurrencyProducer.sendAssetRateMessage(assetRate);


        return rate;
    }


    public void saveAssetTransaction(AssetTransaction assetTransaction) {
        kafkaCurrencyProducer.sendAssetTransactionMessage(assetTransaction);
    }

    public List<AssetTransaction> getAllAssetTransactions(){
        List<AssetTransaction> assets = assetsRepository.getAllAssetTransactions();
        return assets;
    }


    public void removeAsset(String timestamp, String longName, String shortName, String transactionType, AssetClass assetClass){
        assetsRepository.removeAssetTransaction(timestamp,longName,shortName,transactionType, assetClass);
    }




}
