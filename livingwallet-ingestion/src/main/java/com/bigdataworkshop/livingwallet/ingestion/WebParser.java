package com.bigdataworkshop.livingwallet.ingestion;

import org.jsoup.nodes.Document;

public interface WebParser {

    Document parseUrl(String url);
}
