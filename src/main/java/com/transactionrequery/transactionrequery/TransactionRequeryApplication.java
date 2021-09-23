package com.transactionrequery.transactionrequery;

import com.transactionrequery.transactionrequery.dtos.SterlingRequerySearchParams;
import com.transactionrequery.transactionrequery.encrypter.AesEncrypter;
import com.transactionrequery.transactionrequery.services.SterlingTransactionService;
import com.transactionrequery.transactionrequery.utils.StringUtil;
import com.transactionrequery.transactionrequery.utils.TransactionHash;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@SpringBootApplication
@Slf4j
public class TransactionRequeryApplication implements CommandLineRunner {

	@Autowired
	private SterlingTransactionService sterlingTransactionService;

	@Autowired
	private AesEncrypter aesEncrypter;

	private static final String ENCRYPTED_DATA_OUTPUT_FILE_NAME = "output.txt";

	private static final String WEBHOOK_QUERY_OUTPUT_FILE_NAME = "webhook.sql";

	public static void main(String[] args) {
		SpringApplication.run(TransactionRequeryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		handleTransactionLookUp();
		encryptData("MK_TEST_BZHKVYYZ9E", "A7BU7RA3GAFR97HFLNRRKUB49UUR7RFT",
				"https://pay-cloud-web-hwtest2.jirongyunke.net/paycloud/payOutNotify/monnify_13_1721000000507871", 6014);
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

	private void encryptData(String userName, String password, String webhookUrl, Integer merchantId) throws Exception {
		Map<String, String> hashMap = new HashMap<>();
		hashMap.put("USERNAME", userName);
		hashMap.put("PASSWORD", password);

		String result = aesEncrypter.encrypt(StringUtil.objectToString(hashMap));
		try (PrintWriter out = new PrintWriter(new File(ENCRYPTED_DATA_OUTPUT_FILE_NAME))) {
			out.println(result);
		}
		log.info("Encrypted -> {} Decrypted response {}", result, aesEncrypter.decrypt(result));
		String disbursementQuery = buildDisbursementInsertQuery(result, webhookUrl, merchantId);
		writeDisbursementWebhookQueryToFile(disbursementQuery);
	}

	private void writeDisbursementWebhookQueryToFile(String disbursementQuery) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(new File(WEBHOOK_QUERY_OUTPUT_FILE_NAME))) {
			out.println(disbursementQuery);
		}
	}

	private String buildDisbursementInsertQuery(String encryptedCredentials, String webhookUrl, Integer merchantId) {
		StringBuilder baseQuery = new StringBuilder("INSERT INTO merchant_event_configuration(event_type, event_url, merchant_id, authentication_type, authentication_credentials,created_on,last_modified_on," +
				"created_by, modified_by, status, event_category) \nVALUES('SUCCESSFUL_DISBURSEMENT'");
		baseQuery.append(String.format(",\"%s\",%d,'BASIC_AUTH',\n\"%s\",current_timestamp(),current_timestamp(),'LAHRAY', 'LAHRAY', 'ACTIVE','DISBURSEMENT');",webhookUrl, merchantId, encryptedCredentials));
		return baseQuery.toString();
	}
}
