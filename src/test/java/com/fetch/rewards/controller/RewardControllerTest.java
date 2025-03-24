package com.fetch.rewards.controller;

import com.fetch.rewards.model.Receipt;
import com.fetch.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
    }

    @Test
    void processReceipt_shouldReturnReceiptId() throws Exception {
        // Given
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        UUID generatedId = UUID.randomUUID();
        receipt.setId(generatedId);

        when(rewardService.saveReceipt(any(Receipt.class))).thenReturn(receipt);

        // When & Then
        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"retailer\": \"Target\", \"purchaseDate\": \"2022-01-01\", \"purchaseTime\": \"13:01\", \"total\": 35.35 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(generatedId.toString()));

        verify(rewardService, times(1)).saveReceipt(any(Receipt.class));
    }

    @Test
    void getPoints_shouldReturnPoints() throws Exception {
        // Given
        UUID receiptId = UUID.randomUUID();
        Receipt receipt = new Receipt();
        receipt.setId(receiptId);
        when(rewardService.getReceiptById(receiptId)).thenReturn(receipt);
        when(rewardService.calculatePoints(receipt)).thenReturn(28);

        // When & Then
        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(28));

        verify(rewardService, times(1)).getReceiptById(receiptId);
        verify(rewardService, times(1)).calculatePoints(receipt);
    }
}

