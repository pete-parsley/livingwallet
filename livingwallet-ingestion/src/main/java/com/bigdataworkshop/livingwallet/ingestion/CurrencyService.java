package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CurrencyService {

    @Autowired
    private CurrencyParser currencyParser;
    @Autowired
    private DBWriter dbWriter;


    public CurrencyService() {

    }

    public String getCurrencyRate(Currency currency) {
        String rate = currencyParser.getRate(currency);
        dbWriter.storeDataPoint(Float.parseFloat(rate));
        return rate;
    }




}
