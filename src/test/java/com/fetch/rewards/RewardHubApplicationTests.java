package com.fetch.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.fetch.rewards.model.Receipt;
import com.fetch.rewards.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardHubApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ReceiptRepository receiptRepository; // Ensure this is correctly autowired

	@BeforeEach
	public void setUp() {
		// Clean the repository before each test
		receiptRepository.deleteAll();
	}
	@Test
	public void testProcessReceiptEndToEnd() {
		// Prepare the test data
		Receipt receipt = new Receipt();
		receipt.setRetailer("Target");
		receipt.setTotal(35.35);

		// Send POST request to /receipts/process
		var response = restTemplate.postForObject("http://localhost:" + port + "/receipts/process", receipt, String.class);

		// Check if the response contains an ID, indicating success
		assertNotNull(response);
	}
}

