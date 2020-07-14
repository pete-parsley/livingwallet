package com.bigdataworkshop.livingwallet.ingestion;

import com.bigdataworkshop.wallet.model.Currency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.imageio.IIOException;
import java.io.IOException;

@Component
public class CurrencyParser implements WebParser{

    private static final String currencyBaseUrl= "http://stooq.pl";


    @Override
    public String getRate(Currency currency) {

        Document doc = new Document(currencyBaseUrl);
        String parseUrl;
        String rateString = "";

        try {

            switch (currency) {

                case USD:
                    parseUrl = currencyBaseUrl + "/q/?s=usdpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseDoc(doc,"usd");
                    break;
                case EUR:
                    parseUrl = currencyBaseUrl + "/q/?s=eurpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseDoc(doc,"eur");
                    break;
                case CHF:
                    parseUrl = currencyBaseUrl + "/q/?s=chfpln";
                    doc = Jsoup.connect(parseUrl).get();
                    rateString = parseDoc(doc,"chf");
                    break;
                default:
                    break;
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }


        return rateString;
    }

    private String parseDoc(Document document,String currency) {
        Element currencyRateElement =  document.getElementById("aq_" + currency + "pln_c5");
        return currencyRateElement.text();
    }
}
