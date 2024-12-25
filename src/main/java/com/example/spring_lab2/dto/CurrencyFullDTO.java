package com.example.spring_lab2.dto;

public class CurrencyFullDTO {
    private Long id;
    private String currencyName;
    private java.time.LocalDate createDate;
    private java.util.List<CurrencyRateDTO> rates;

    public CurrencyFullDTO(Long id, String currencyName, java.time.LocalDate createDate, java.util.List<CurrencyRateDTO> rates) {
        this.id = id;
        this.currencyName = currencyName;
        this.createDate = createDate;
        this.rates = rates;
    }

    public Long getId() {
        return id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public java.time.LocalDate getCreateDate() {
        return createDate;
    }

    public java.util.List<CurrencyRateDTO> getRates() {
        return rates;
    }
}