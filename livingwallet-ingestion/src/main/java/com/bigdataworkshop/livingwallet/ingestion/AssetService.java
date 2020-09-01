package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.AssetTransaction;
import com.bigdataworkshop.wallet.model.Currency;
import com.bigdataworkshop.wallet.model.Metals;
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
            List<String> requiredCurrencies = getRequiredAssetRates("CURRENCY");
            for (String currency : requiredCurrencies) {
                logger.info("Getting rate for: " + currency);
                saveAssetRate(Currency.valueOf(currency));
            }
        }
    }

    private List<String> getRequiredAssetRates(String assetClass){
        List<String> requiredCurrencies = new ArrayList<>();
        List<AssetTransaction> assetTransactions = assetsRepository.getAllCurrencyAssetTransactions();

        for(AssetTransaction assetTransaction : assetTransactions){
            if(!requiredCurrencies.contains(assetTransaction.getAssetShortName()))
                requiredCurrencies.add(assetTransaction.getAssetShortName());
        }

        return requiredCurrencies;
    }



    public String saveAssetRate(Currency currency) {
        String rate = assetParser.getRate(currency);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Instant dateobj = Instant.now();
        AssetTransaction currencyAssetTransaction = new AssetTransaction(Float.parseFloat(rate), dateobj, currency.toString(),"kurs wymiany walut","notowania",1.0f,"CURRENCY", LocalDate.now(),"");
        kafkaCurrencyProducer.sendCurrencyRateMessage(currencyAssetTransaction);
        return rate;
    }


    public void saveAssetTransaction(AssetTransaction currencyAssetTransaction) {
        kafkaCurrencyProducer.sendCurrencyAssetMessage(currencyAssetTransaction);
    }

    public List<AssetTransaction> getAllAssetTransactions(String assetClass){
        List<AssetTransaction> curr = assetsRepository.getAllCurrencyAssetTransactions();
        return curr;
    }


    public void removeAsset(String timestamp, String longName, String shortName, String transactionType){
        assetsRepository.removeAssetTransaction(timestamp,longName,shortName,transactionType);
    }




}
