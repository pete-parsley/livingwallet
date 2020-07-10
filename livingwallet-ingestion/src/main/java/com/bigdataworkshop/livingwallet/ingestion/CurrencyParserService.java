package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class CurrencyParserService implements WebParser {

    private Currency searchCurrency;

    public CurrencyParserService(Currency searchCurrency) {
        this.searchCurrency = searchCurrency;
    }

    @Override
    public Document parseUrl(String url) {
        return null;
    }

    public float getCurrencyRate(Document page) {

        return 0.0f;
    }


}
