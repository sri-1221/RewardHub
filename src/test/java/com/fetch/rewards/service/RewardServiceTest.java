package com.fetch.rewards.service;

import com.fetch.rewards.model.Item;
import com.fetch.rewards.model.Receipt;
import com.fetch.rewards.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceTest {

    @InjectMocks
    private RewardService rewardService;

    @Mock
    private ReceiptRepository receiptRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculatePoints_shouldReturnCorrectPoints() {
        // Given
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setTotal(35.35);
        receipt.setPurchaseDate("2022-01-01");
        receipt.setPurchaseTime("13:01");

        Item item1 = new Item();
        item1.setShortDescription("Mountain Dew 12PK");
        item1.setPrice(6.49);
        Item item2 = new Item();
        item2.setShortDescription("Emils Cheese Pizza");
        item2.setPrice(12.25);
        Item item3 = new Item();
        item3.setShortDescription("Knorr Creamy Chicken");
        item3.setPrice(1.26);
        Item item4 = new Item();
        item4.setShortDescription("Doritos Nacho Cheese");
        item4.setPrice(3.35);
        Item item5 = new Item();
        item5.setShortDescription("   Klarbrunn 12-PK 12 FL OZ  ");
        item5.setPrice(12.00);

        receipt.setItems(Arrays.asList(item1, item2,item3,item4,item5));

        // When
        int points = rewardService.calculatePoints(receipt);

        // Then
        assertEquals(28, points);
    }

    @Test
    void saveReceipt_shouldReturnSavedReceipt() {
        // Given
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");

        // Mocking the repository save method
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        // When
        Receipt savedReceipt = rewardService.saveReceipt(receipt);

        // Then
        assertNotNull(savedReceipt);
        assertEquals("Target", savedReceipt.getRetailer());
        verify(receiptRepository, times(1)).save(receipt);
    }

    @Test
    void getReceiptById_shouldReturnReceipt() {
        // Given
        UUID receiptId = UUID.randomUUID();
        Receipt receipt = new Receipt();
        when(receiptRepository.findById(receiptId)).thenReturn(java.util.Optional.of(receipt));

        // When
        Receipt fetchedReceipt = rewardService.getReceiptById(receiptId);

        // Then
        assertNotNull(fetchedReceipt);
        verify(receiptRepository, times(1)).findById(receiptId);
    }

    @Test
    void getReceiptById_shouldReturnNull_whenNotFound() {
        // Given
        UUID receiptId = UUID.randomUUID();
        when(receiptRepository.findById(receiptId)).thenReturn(java.util.Optional.empty());

        // When
        Receipt fetchedReceipt = rewardService.getReceiptById(receiptId);

        // Then
        assertNull(fetchedReceipt);
    }
}
