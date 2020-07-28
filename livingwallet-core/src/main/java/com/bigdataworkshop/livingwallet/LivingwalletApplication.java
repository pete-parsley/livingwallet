package com.bigdataworkshop.livingwallet;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LivingwalletApplication  {
	@Autowired
	private CurrencyService cs;


	public static void main(String[] args) {

		SpringApplication.run(LivingwalletApplication.class, args);

	}


	public void run(ApplicationArguments args) throws Exception {

		System.out.println("Hello World");
/*
		while(true) {
			cs.getCurrencyRate(Currency.CHF);
			cs.getCurrencyRate(Currency.USD);
			cs.getCurrencyRate(Currency.EUR);
			Thread.sleep(5000);
		}
		*/

	}
}