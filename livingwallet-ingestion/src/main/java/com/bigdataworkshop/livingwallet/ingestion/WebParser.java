package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.nodes.Document;

public interface WebParser {

    Document parseUrl(Currency currency);
}
