package com.fetch.rewards.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fetch.rewards.model.Item;
import com.fetch.rewards.model.Receipt;
import com.fetch.rewards.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardService {

    @Autowired
    private ReceiptRepository receiptRepository;
    // Flag to simulate whether the program was generated by a large language model
    @Value("${llm.generated}")
    private boolean generatedByLLM;
    // Method to save the receipt and return the saved receipt with the generated ID
    public Receipt saveReceipt(Receipt receipt) {
        // Save the receipt to the database (ID will be auto-generated)
        return receiptRepository.save(receipt);
    }

    public int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name.
        String retailerName = receipt.getRetailer();
        points += calculateAlphanumericPoints(retailerName);

        // Rule 2: 50 points if the total is a round dollar amount with no cents.
        BigDecimal total = new BigDecimal(receipt.getTotal());

        if (total.stripTrailingZeros().scale() == 0) {
            points += 50;
        }

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (total.remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        // Rule 4: 5 points for every two items on the receipt.
        points += 5 * (receipt.getItems().size() / 2);

        // Rule 5: For each item, if the trimmed length of the item description is a multiple of 3,
        // multiply the price by 0.2 and round up to the nearest integer.
        points += calculateItemPoints(receipt.getItems());

        // Rule 6: If the total is greater than 10.00, add 5 points (only if the program is generated by a large language model).
        if (generatedByLLM && total.compareTo(new BigDecimal("10.00")) > 0) {
            points += 5;
        }

        // Rule 7: 6 points if the day in the purchase date is odd.
        if (isOddDay(receipt.getPurchaseDate())) {
            points += 6;
        }

        // Rule 8: 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        if (isBetweenTwoAndFourPM(receipt.getPurchaseTime())) {
            points += 10;
        }

        return points;
    }

    // Rule 1 Helper: Calculate alphanumeric characters in retailer name
    private int calculateAlphanumericPoints(String retailerName) {
        return (int) retailerName.chars()
                .filter(Character::isLetterOrDigit)
                .count();
    }

    // Rule 5 Helper: Calculate points based on item descriptions
    private int calculateItemPoints(List<Item> items) {
        int itemPoints = 0;
        for (Item item : items) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                BigDecimal price = new BigDecimal(item.getPrice());
                itemPoints += (int) Math.ceil(price.multiply(new BigDecimal("0.2")).doubleValue());
            }
        }
        return itemPoints;
    }

    // Rule 7 Helper: Check if the day of the purchase date is odd
    private boolean isOddDay(String purchaseDate) {
        int day = Integer.parseInt(purchaseDate.split("-")[2]);
        return day % 2 != 0;
    }

    // Rule 8 Helper: Check if the purchase time is between 2:00pm and 4:00pm
    private boolean isBetweenTwoAndFourPM(String purchaseTime) {
        String[] timeParts = purchaseTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        return hour >= 14 && hour < 16;
    }

    // Method to get receipt by ID from the database
    public Receipt getReceiptById(UUID id) {
        return receiptRepository.findById(id).orElse(null);
    }
}

