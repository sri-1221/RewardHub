package com.fetch.rewards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "Item description cannot be null")
    @Size(min = 1, message = "Item description must have at least one character")
    private String shortDescription;

    @Positive(message = "Item price must be positive")
    private double price;

    @ManyToOne
    private Receipt receipt;
}

