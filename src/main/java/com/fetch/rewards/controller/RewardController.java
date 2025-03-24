package com.fetch.rewards.controller;

import com.fetch.rewards.exception.ReceiptNotFoundException;
import com.fetch.rewards.model.Receipt;
import com.fetch.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody Receipt receipt) {
        if (receipt == null || receipt.getRetailer() == null || receipt.getRetailer().isEmpty()) {
            throw new IllegalArgumentException("Invalid receipt data. Retailer is required.");
        }

        Receipt savedReceipt = rewardService.saveReceipt(receipt);

        // Return a map with the id of the saved receipt
        Map<String, String> response = new HashMap<>();
        response.put("id", savedReceipt.getId().toString()); // Ensure it's a String

        return ResponseEntity.ok(response); // Return ResponseEntity with Map<String, String>
    }

    // Endpoint to get the points of a receipt by ID
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, String>> getPoints(@PathVariable UUID id) {
        // Fetch the receipt by ID from the database
        Receipt receipt = rewardService.getReceiptById(id);
        if (receipt == null) {
            throw new ReceiptNotFoundException("Receipt with ID " + id + " not found.");
        }

        // Calculate points for the receipt
        int points = rewardService.calculatePoints(receipt);

        // Prepare the response in the required format (as String)
        Map<String, String> response = new HashMap<>();
        response.put("points", Integer.toString(points)); // Convert points to a String

        return ResponseEntity.ok(response); // Return ResponseEntity with Map<String, String>
    }
}
