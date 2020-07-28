package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import com.bigdataworkshop.wallet.model.CurrencyAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class CurrencyService {

    @Autowired
    private CurrencyParser currencyParser;


    @Autowired
    private KafkaCurrencyProducer kafkaCurrencyProducer;


    public CurrencyService() {

    }

    public String saveCurrencyRate(Currency currency) {
        String rate = currencyParser.getRate(currency);
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        CurrencyAsset currencyAsset = new CurrencyAsset(Float.parseFloat(rate), dateobj, currency.toString() + "->PLN","shortName","longName","description",0.0f);
        kafkaCurrencyProducer.sendCurrencyRateMessage(currencyAsset);
        return rate;
    }

    public void saveCurrencyAsset(CurrencyAsset currencyAsset) {
        kafkaCurrencyProducer.sendCurrencyAssetMessage(currencyAsset);
    }




}
