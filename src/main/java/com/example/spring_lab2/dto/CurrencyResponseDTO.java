package com.example.spring_lab2.dto;

import java.time.LocalDate;

public class CurrencyResponseDTO {
    private Long id;
    private String currencyName;
    private LocalDate createDate;

    public CurrencyResponseDTO(Long id, String currencyName, LocalDate createDate) {
        this.id = id;
        this.currencyName = currencyName;
        this.createDate = createDate;
    }

    // Getters only
    public Long getId() { return id; }
    public String getCurrencyName() { return currencyName; }
    public LocalDate getCreateDate() { return createDate; }
}