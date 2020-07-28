package com.bigdataworkshop.livingwallet.web;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.CurrencyAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/currency")
    public String currency(Model model) {
        logger.info("Redirecting to index.html");
        model.addAttribute("currency",new CurrencyAsset());
        return "currency";
    }


    @PostMapping("/currency")
    public String currencyForm(@ModelAttribute CurrencyAsset currencyAsset) {
        logger.info("Submitted Currency: " + currencyAsset.toString());
        currencyService.saveCurrencyAsset(currencyAsset);
        return "index";
    }


}