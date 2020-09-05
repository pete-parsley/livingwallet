package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetClass;
import com.bigdataworkshop.wallet.model.AssetTransaction;
import com.bigdataworkshop.wallet.model.Currency;
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
            List<String> requiredCurrencies = getRequiredAssetRates(AssetClass.CURRENCY);
            for (String currency : requiredCurrencies) {
                logger.info("Getting rate for: " + currency);
                saveCurrencyAssetRate(Currency.valueOf(currency));
            }
        }
    }

    private List<String> getRequiredAssetRates(AssetClass assetClass){
        List<String> requiredAsset = new ArrayList<>();

        List<AssetTransaction> assetTransactions = assetsRepository.getAllAssetTransactions(assetClass);

        for(AssetTransaction assetTransaction : assetTransactions){
            if(!requiredAsset.contains(assetTransaction.getAssetShortName()))
                requiredAsset.add(assetTransaction.getAssetShortName());
        }

        return requiredAsset;
    }




    public String saveCurrencyAssetRate(Currency currency) {
        String rate = assetParser.getCurrencyRate(currency);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Instant dateobj = Instant.now();
        AssetTransaction currencyAssetTransaction = new AssetTransaction(Float.parseFloat(rate), dateobj, currency.toString(),"kurs wymiany walut","notowania",1.0f,"CURRENCY", LocalDate.now(),"");
        kafkaCurrencyProducer.sendAssetRateMessage(currencyAssetTransaction);
        return rate;
    }


    public void saveAssetTransaction(AssetTransaction assetTransaction) {
        kafkaCurrencyProducer.sendAssetTransactionMessage(assetTransaction);
    }

    public List<AssetTransaction> getAllAssetTransactions(AssetClass assetClass){
        List<AssetTransaction> assets = assetsRepository.getAllAssetTransactions(assetClass);
        return assets;
    }


    public void removeAsset(String timestamp, String longName, String shortName, String transactionType, AssetClass assetClass){
        assetsRepository.removeAssetTransaction(timestamp,longName,shortName,transactionType, assetClass);
    }




}
