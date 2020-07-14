package com.bigdataworkshop.livingwallet;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LivingwalletApplication implements ApplicationRunner {
	@Autowired
	private CurrencyService cs;

	public static void main(String[] args) {

		SpringApplication.run(LivingwalletApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Currency cur = Currency.USD;
		cs.setSearchCurrency(cur);
		System.out.println(cs.getCurrencyRate());
	}
}