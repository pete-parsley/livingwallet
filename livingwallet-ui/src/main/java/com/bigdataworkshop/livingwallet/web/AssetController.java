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

    @GetMapping("/asset_create")
    public String currencyBuy(Model model) {
        logger.info("Redirecting to asset_create.html");
        AssetTransaction assetTransaction = new AssetTransaction();
        assetTransaction.setAssetClass("CURRENCY");
        assetTransaction.setTransactionType("BUY");
        model.addAttribute("asset", assetTransaction);
        return "asset_create";
    }


    @PostMapping("/asset_create")
    public String currencyBuyForm(@ModelAttribute AssetTransaction assetTransaction) {
        logger.info("Submitted Currency: " + assetTransaction.toString());
        assetService.saveAssetTransaction(assetTransaction);
        return "index";
    }


    @GetMapping("/asset_mod")
    public String currencyModify(Model model) {
        logger.info("Redirecting to asset_mod.html");
        List<AssetTransaction> assets = assetService.getAllAssetTransactions();
        model.addAttribute("assets", assets);
        return "asset_mod";
    }

    @GetMapping("/asset_remove")
    public String currencyRemove(@RequestParam(name="timestamp") String timestamp, @RequestParam(name="long_name") String longName, @RequestParam(name="short_name") String shortName, @RequestParam(name="transaction_type") String transactionType, Model model) {
        logger.info("Redirecting to asset_remove.html");
        assetService.removeAsset(timestamp,longName,shortName,transactionType,AssetClass.CURRENCY);
        List<AssetTransaction> assets = assetService.getAllAssetTransactions();
        model.addAttribute("assets", assets);
        return "asset_mod";
    }



}