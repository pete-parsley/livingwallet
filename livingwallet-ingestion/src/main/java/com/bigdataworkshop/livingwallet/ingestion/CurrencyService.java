package com.bigdataworkshop.livingwallet.ingestion;

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
public class CurrencyService {

    Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    @Autowired
    private CurrencyParser currencyParser;


    @Autowired
    private KafkaCurrencyProducer kafkaCurrencyProducer;

    @Autowired
    private AssetsRepository assetsRepository;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;


    public CurrencyService() {

    }


    @Scheduled(fixedDelay = 10000)
    public void getRates(){
        if(!activeProfile.equals("dev")) {
            logger.info("Checking currency rates");
            List<String> requiredCurrencies = getRequiredCurrencyRates();
            for (String currency : requiredCurrencies) {
                logger.info("Getting rate for: " + currency);
                saveCurrencyRate(Currency.valueOf(currency));
            }
        }
    }

    private List<String> getRequiredCurrencyRates(){
        List<String> requiredCurrencies = new ArrayList<>();
        List<AssetTransaction> assetTransactions = assetsRepository.getAllCurrencyAssetTransactions();

        for(AssetTransaction assetTransaction : assetTransactions){
            if(!requiredCurrencies.contains(assetTransaction.getAssetShortName()))
                requiredCurrencies.add(assetTransaction.getAssetShortName());
        }
        return requiredCurrencies;
    }

    public String saveCurrencyRate(Currency currency) {
        String rate = currencyParser.getRate(currency);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Instant dateobj = Instant.now();
        AssetTransaction currencyAssetTransaction = new AssetTransaction(Float.parseFloat(rate), dateobj, currency.toString(),"kurs wymiany walut","notowania",1.0f,"CURRENCY", LocalDate.now(),"");
        kafkaCurrencyProducer.sendCurrencyRateMessage(currencyAssetTransaction);
        return rate;
    }

    public void saveCurrencyTransaction(AssetTransaction currencyAssetTransaction) {
        kafkaCurrencyProducer.sendCurrencyAssetMessage(currencyAssetTransaction);
    }

    public List<AssetTransaction> getAllCurrencyAssetTransactions(){
        List<AssetTransaction> curr = assetsRepository.getAllCurrencyAssetTransactions();
        return curr;
    }

    public void removeCurrencyAsset(String timestamp, String longName, String shortName, String transactionType){
        assetsRepository.removeCurrencyAsset(timestamp,longName,shortName,transactionType);
    }




}
