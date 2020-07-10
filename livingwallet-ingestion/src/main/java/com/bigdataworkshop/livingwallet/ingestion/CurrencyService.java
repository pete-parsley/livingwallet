package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CurrencyService {

    @Autowired
    private CurrencyParser currencyParser;

    private Currency searchCurrency;

    public CurrencyService() {
    }

    public Currency getSearchCurrency() {
        return searchCurrency;
    }

    public void setSearchCurrency(Currency searchCurrency) {
        this.searchCurrency = searchCurrency;
    }

    public String getCurrencyRate() {
        return "dupa";
    }




}
