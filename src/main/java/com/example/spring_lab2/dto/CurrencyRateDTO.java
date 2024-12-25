package com.example.spring_lab2.dto;

import java.time.LocalDate;

public class CurrencyRateDTO {
    private LocalDate date;
    private double exchangeRate;
    private double rateChange;
    private double percentageChange;

    public CurrencyRateDTO(LocalDate date, double exchangeRate, double rateChange, double percentageChange) {
        this.date = date;
        this.exchangeRate = exchangeRate;
        this.rateChange = rateChange;
        this.percentageChange = percentageChange;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public double getRateChange() {
        return rateChange;
    }

    public double getPercentageChange() {
        return percentageChange;
    }
}