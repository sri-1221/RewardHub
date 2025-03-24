package com.fetch.rewards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Retailer cannot be null")
    @Size(min = 1, message = "Retailer must have at least one character")
    private String retailer;

    @NotNull(message = "Purchase date cannot be null")
    private String purchaseDate;

    @NotNull(message = "Purchase time cannot be null")
    private String purchaseTime;

    @Positive(message = "Total amount must be positive")
    private double total;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> items;
}
