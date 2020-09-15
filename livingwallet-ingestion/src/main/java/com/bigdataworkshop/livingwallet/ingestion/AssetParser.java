package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import com.bigdataworkshop.wallet.model.Metals;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AssetParser {

    private static final String baseUrl = "http://stooq.pl";


    public String getCurrencyRate(Currency currency) {

        Document doc = new Document(baseUrl);
        String parseUrl;
        String rateString = "";

        try {

            switch (currency) {

                case USD:
                    parseUrl = baseUrl + "/q/?s=usdpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseCurrencyDoc(doc,"usd");
                    break;
                case EUR:
                    parseUrl = baseUrl + "/q/?s=eurpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseCurrencyDoc(doc,"eur");
                    break;
                case CHF:
                    parseUrl = baseUrl + "/q/?s=chfpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseCurrencyDoc(doc,"chf");
                    break;
                default:
                    break;
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }


        return rateString;
    }

    public String getMetalRate(Metals metal) {

        Document doc = new Document(baseUrl);
        String parseUrl;
        String rateString = "";

        try {

            switch (metal) {

                case GOLD:
                    parseUrl = baseUrl + "/q/?s=xaupln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseMetalDoc(doc,"xau");
                    break;
                case SILVER:
                    parseUrl = baseUrl + "/q/?s=xagpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseMetalDoc(doc,"xag");
                    break;
                default:
                    break;
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }


        return rateString;
    }



    private String parseCurrencyDoc(Document document, String currency) {
        Element currencyRateElement =  document.getElementById("aq_" + currency + "pln_c5");
        return currencyRateElement.text();
    }

    private String parseMetalDoc(Document document, String metal) {
        Element metalRateElement;

        switch (metal) {
            case "xau":
                metalRateElement =  document.getElementById("aq_" + metal + "pln_c2");
                break;

            case "xag":
                metalRateElement =  document.getElementById("aq_" + metal + "pln_c3");
                break;
            default:
                throw new IllegalArgumentException("Wrong metal name");
        }

        return metalRateElement.text();
    }
}
