package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class CurrencyParser implements WebParser{

    private static final String url= "http://";


    @Override
    public Document parseUrl(Currency currency) {
        return null;
    }
}
