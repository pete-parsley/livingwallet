package com.bigdataworkshop.livingwallet;

import com.bigdataworkshop.livingwallet.ingestion.CurrencyService;
import com.bigdataworkshop.wallet.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;


@SpringBootApplication
@EnableScheduling
public class LivingwalletApplication  {
	@Autowired
	private CurrencyService cs;

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}


	public static void main(String[] args) {

		SpringApplication.run(LivingwalletApplication.class, args);

	}


	public void run(ApplicationArguments args) throws Exception {

		System.out.println("Hello World");

		while(true) {
			cs.saveCurrencyRate(Currency.CHF);
			cs.saveCurrencyRate(Currency.USD);
			cs.saveCurrencyRate(Currency.EUR);
			Thread.sleep(5000);
		}


	}
}