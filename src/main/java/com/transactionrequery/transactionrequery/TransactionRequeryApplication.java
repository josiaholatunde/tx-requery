package com.transactionrequery.transactionrequery;

import com.transactionrequery.transactionrequery.dtos.SterlingRequerySearchParams;
import com.transactionrequery.transactionrequery.services.SterlingTransactionService;
import com.transactionrequery.transactionrequery.utils.TransactionHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintWriter;
import java.util.*;

@SpringBootApplication
@Slf4j
public class TransactionRequeryApplication implements CommandLineRunner {

	@Autowired
	private SterlingTransactionService sterlingTransactionService;

	public static void main(String[] args) {
		SpringApplication.run(TransactionRequeryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		handleTransactionLookUp();
//		log.info("Yo -> Result {}", result);
	}

	private void handleTransactionLookUp() throws Exception {
		log.info("Started script");
		SterlingRequerySearchParams sterlingRequerySearchParams = SterlingRequerySearchParams.builder()
				.sessionIds(Arrays.asList(

				)).build();
		String result = sterlingTransactionService.queryBySearchParams(sterlingRequerySearchParams);
		if (!Strings.isBlank(result)) {
			log.info("Result -> {}", result);
			try (PrintWriter out = new PrintWriter("output2.sql")) {
				out.println(result);
			}
		}
		log.info("Finished script Result {}", result);
	}
}
