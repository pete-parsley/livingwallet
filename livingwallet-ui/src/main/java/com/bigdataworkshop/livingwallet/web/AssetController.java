package com.bigdataworkshop.livingwallet.web;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AssetController {

    @Autowired
    CurrencyService currencyService;

    private final Logger logger = LoggerFactory.getLogger(AssetController.class);


    @GetMapping("/home")
    public String homePage() {
        logger.info("Redirecting to index.html");
        return "index";
    }

    @GetMapping("/currency_buy")
    public String currency(Model model) {
        logger.info("Redirecting to currency_buy.html");
        Asset currencyAsset = new Asset();
        currencyAsset.setAssetClass("CURRENCY");
        model.addAttribute("currency",currencyAsset);
        return "currency_buy";
    }


    @PostMapping("/currency_buy")
    public String currencyForm(@ModelAttribute Asset currencyAsset) {
        logger.info("Submitted Currency: " + currencyAsset.toString());
      //  currencyAsset.setPricingDate(currencyAsset.getPricingDateFormatted().toInstant());
        currencyService.saveCurrencyAsset(currencyAsset);
        return "index";
    }

    @GetMapping("/currency_mod")
    public String currencyModify(Model model) {
        logger.info("Redirecting to currency_mod.html");
        List<Asset> curr = currencyService.getAllCurrencyAssetTransactions();
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }

    @GetMapping("/currency_remove")
    public String currencyRemove(@RequestParam(name="timestamp") String timestamp, @RequestParam(name="long_name") String longName, @RequestParam(name="short_name") String shortName, Model model) {
        logger.info("Redirecting to currency_remove.html");
        currencyService.removeCurrencyAsset(timestamp,longName,shortName);
        List<Asset> curr = currencyService.getAllCurrencyAssetTransactions();
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }


}