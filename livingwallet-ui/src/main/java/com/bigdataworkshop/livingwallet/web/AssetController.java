package com.bigdataworkshop.livingwallet.web;

import com.bigdataworkshop.livingwallet.ingestion.AssetService;
import com.bigdataworkshop.wallet.model.AssetClass;
import com.bigdataworkshop.wallet.model.AssetTransaction;
import com.bigdataworkshop.wallet.model.TransactionType;
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
    AssetService assetService;

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
        currencyAssetTransaction.setAssetClass(AssetClass.CURRENCY.toString());
        currencyAssetTransaction.setTransactionType(TransactionType.BUY.toString());
        model.addAttribute("currency", currencyAssetTransaction);
        return "currency_buy";
    }


    @PostMapping("/currency_buy")
    public String currencyBuyForm(@ModelAttribute AssetTransaction currencyAssetTransaction) {
        logger.info("Submitted Currency: " + currencyAssetTransaction.toString());
        assetService.saveAssetTransaction(currencyAssetTransaction);
        return "index";
    }

    @GetMapping("/currency_sell")
    public String currencySell(Model model) {
        logger.info("Redirecting to currency_sell.html");
        AssetTransaction currencyAssetTransaction = new AssetTransaction();
        currencyAssetTransaction.setAssetClass(AssetClass.CURRENCY.toString());
        currencyAssetTransaction.setTransactionType(TransactionType.SELL.toString());
        model.addAttribute("currency", currencyAssetTransaction);
        return "currency_sell";
    }


    @PostMapping("/currency_sell")
    public String currencySellForm(@ModelAttribute AssetTransaction currencyAssetTransaction) {
        logger.info("Submitted Currency: " + currencyAssetTransaction.toString());
        assetService.saveAssetTransaction(currencyAssetTransaction);
        return "index";
    }

    @GetMapping("/currency_mod")
    public String currencyModify(Model model) {
        logger.info("Redirecting to currency_mod.html");
        List<AssetTransaction> curr = assetService.getAllAssetTransactions(AssetClass.CURRENCY);
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }

    @GetMapping("/currency_remove")
    public String currencyRemove(@RequestParam(name="timestamp") String timestamp, @RequestParam(name="long_name") String longName, @RequestParam(name="short_name") String shortName, @RequestParam(name="transaction_type") String transactionType, Model model) {
        logger.info("Redirecting to currency_remove.html");
        assetService.removeAsset(timestamp,longName,shortName,transactionType,AssetClass.CURRENCY);
        List<AssetTransaction> curr = assetService.getAllAssetTransactions(AssetClass.CURRENCY);
        model.addAttribute("currencies", curr);
        return "currency_mod";
    }

    @GetMapping("/metal_buy")
    public String metalBuy(Model model) {
        logger.info("Redirecting to metal_buy.html");
        AssetTransaction assetTransaction = new AssetTransaction();
        assetTransaction.setAssetClass(AssetClass.METAL.toString());
        assetTransaction.setTransactionType(TransactionType.BUY.toString());
        model.addAttribute("metal", assetTransaction);
        return "metal_buy";
    }


    @PostMapping("/metal_buy")
    public String metalBuyForm(@ModelAttribute AssetTransaction metalAssetTransaction) {
        logger.info("Submitted Metal: " + metalAssetTransaction.toString());
        assetService.saveAssetTransaction(metalAssetTransaction);
        return "index";
    }

    @GetMapping("/metal_sell")
    public String metalSell(Model model) {
        logger.info("Redirecting to metal_sell.html");
        AssetTransaction currencyAssetTransaction = new AssetTransaction();
        currencyAssetTransaction.setAssetClass(AssetClass.METAL.toString());
        currencyAssetTransaction.setTransactionType(TransactionType.SELL.toString());
        model.addAttribute("metal", currencyAssetTransaction);
        return "metal_sell";
    }

    @PostMapping("/metal_sell")
    public String metalSellForm(@ModelAttribute AssetTransaction metalAssetTransaction) {
        logger.info("Submitted Metal: " + metalAssetTransaction.toString());
        assetService.saveAssetTransaction(metalAssetTransaction);
        return "index";
    }

    @GetMapping("/metal_mod")
    public String metalModify(Model model) {
        logger.info("Redirecting to metal_mod.html");
        List<AssetTransaction> metals = assetService.getAllAssetTransactions(AssetClass.METAL);
        model.addAttribute("metals", metals);
        return "metal_mod";
    }

    @GetMapping("/metal_remove")
    public String metalRemove(@RequestParam(name="timestamp") String timestamp, @RequestParam(name="long_name") String longName, @RequestParam(name="short_name") String shortName, @RequestParam(name="transaction_type") String transactionType, Model model) {
        logger.info("Redirecting to metal_remove.html");
        assetService.removeAsset(timestamp,longName,shortName,transactionType,AssetClass.METAL);
        List<AssetTransaction> metals = assetService.getAllAssetTransactions(AssetClass.METAL);
        model.addAttribute("metals", metals);
        return "metal_mod";
    }


}