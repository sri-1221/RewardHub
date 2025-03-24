package com.fetch.rewards.repository;

import com.fetch.rewards.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    // Custom query methods can be added here if needed
}
