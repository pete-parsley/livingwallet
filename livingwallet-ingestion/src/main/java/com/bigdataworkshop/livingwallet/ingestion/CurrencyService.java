package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Asset;
import com.bigdataworkshop.wallet.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class CurrencyService {

    @Autowired
    private CurrencyParser currencyParser;


    @Autowired
    private KafkaCurrencyProducer kafkaCurrencyProducer;

    @Autowired
    private AssetsRepository assetsRepository;


    public CurrencyService() {

    }

    public String saveCurrencyRate(Currency currency) {
        String rate = currencyParser.getRate(currency);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        Asset currencyAsset = new Asset(Float.parseFloat(rate), dateobj, currency.toString() + "->PLN","kurs wymiany USD na PLN","notowania",1.0f,"CURRENCY");
        kafkaCurrencyProducer.sendCurrencyRateMessage(currencyAsset);
        return rate;
    }

    public void saveCurrencyAsset(Asset currencyAsset) {
        kafkaCurrencyProducer.sendCurrencyAssetMessage(currencyAsset);
    }

    public List<Asset> getAllCurrencyAssets(){
        List<Asset> curr = assetsRepository.getAllCurrencyAssets();
        return curr;
    }




}
