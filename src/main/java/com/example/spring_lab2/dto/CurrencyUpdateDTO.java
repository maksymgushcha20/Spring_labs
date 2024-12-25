package com.example.spring_lab2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO для оновлення інформації про валюту")
public class CurrencyUpdateDTO {
    private String currencyName;
    private LocalDate date;
    private double initialRate;

    public CurrencyUpdateDTO(String currencyName, LocalDate date, double initialRate) {
        this.currencyName = currencyName;
        this.date = date;
        this.initialRate = initialRate;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getInitialRate() {
        return initialRate;
    }

    public void setInitialRate(double initialRate) {
        this.initialRate = initialRate;
    }
}