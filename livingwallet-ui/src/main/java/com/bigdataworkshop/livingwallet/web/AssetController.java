package com.bigdataworkshop.livingwallet.web;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.AssetTransaction;
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
    public String currencyBuy(Model model) {
        logger.info("Redirecting to currency_buy.html");
        AssetTransaction currencyAssetTransaction = new AssetTransaction();
        currencyAssetTransaction.setAssetClass("CURRENCY");
        currencyAssetTransaction.setTransactionType("BUY");
        model.addAttribute("currency", currencyAssetTransaction);
        return "currency_buy";
    }


    @PostMapping("/currency_buy")
    public String currencyBuyForm(@ModelAttribute AssetTransaction currencyAssetTransaction) {
        logger.info("Submitted Currency: " + currencyAssetTransaction.toString());
        currencyService.saveCurrencyTransaction(currencyAssetTransaction);
        return "index";
    }

    @GetMapping("/currency_sell")
    public String currencySell(Model model) {
        logger.info("Redirecting to currency_sell.html");
        AssetTransaction currencyAssetTransaction = new AssetTransaction();
        currencyAssetTransaction.setAssetClass("CURRENCY");
        currencyAssetTransaction.setTransactionType("SELL");
        model.addAttribute("currency", currencyAssetTransaction);
        return "currency_sell";
    }


    @PostMapping("/currency_sell")
    public String currencySellForm(@ModelAttribute AssetTransaction currencyAssetTransaction) {
        logger.info("Submitted Currency: " + currencyAssetTransaction.toString());
        currencyService.saveCurrencyTransaction(currencyAssetTransaction);
        return "index";
    }

    @GetMapping("/currency_mod")
    public String currencyModify(Model model) {
        logger.info("Redirecting to currency_mod.html");
        List<AssetTransaction> curr = currencyService.getAllCurrencyAssetTransactions();
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }

    @GetMapping("/currency_remove")
    public String currencyRemove(@RequestParam(name="timestamp") String timestamp, @RequestParam(name="long_name") String longName, @RequestParam(name="short_name") String shortName, @RequestParam(name="transaction_type") String transactionType, Model model) {
        logger.info("Redirecting to currency_remove.html");
        currencyService.removeCurrencyAsset(timestamp,longName,shortName,transactionType);
        List<AssetTransaction> curr = currencyService.getAllCurrencyAssetTransactions();
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }


}