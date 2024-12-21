package com.example.spring_lab2.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Column(name = "rate_date", nullable = false)
    private LocalDate date;

    @Column(name = "exchange_rate", nullable = false)
    private double exchangeRate;

    @Column(name = "previous_rate")
    private double previousRate;

    @Column(name = "rate_change")
    private double rateChange;


    public CurrencyRate() {
    }


    public CurrencyRate(String currencyName, LocalDate date, double exchangeRate, double previousRate, double rateChange) {
        this.currencyName = currencyName;
        this.date = date;
        this.exchangeRate = exchangeRate;
        this.previousRate = previousRate;
        this.rateChange = rateChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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