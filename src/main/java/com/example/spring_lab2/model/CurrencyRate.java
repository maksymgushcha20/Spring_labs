package com.example.spring_lab2.model;

import java.time.LocalDate;

public class CurrencyRate {
    private String currencyName;
    private LocalDate date;
    private double exchangeRate;
    private double previousRate;
    private double rateChange;

    public CurrencyRate(String currencyName, LocalDate date, double exchangeRate, double previousRate, double rateChange) {
        this.currencyName = currencyName;
        this.date = date;
        this.exchangeRate = exchangeRate;
        this.previousRate = previousRate;
        this.rateChange = rateChange;
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

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getPreviousRate() {
        return previousRate;
    }

    public void setPreviousRate(double previousRate) {
        this.previousRate = previousRate;
    }

    public double getRateChange() {
        return rateChange;
    }

    public void setRateChange(double rateChange) {
        this.rateChange = rateChange;
    }
}
