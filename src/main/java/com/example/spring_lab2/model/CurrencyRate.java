package com.example.spring_lab2.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rates")
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "rate_date", nullable = false)
    private LocalDate date;

    @Column(name = "exchange_rate", nullable = false)
    private double exchangeRate;

    public CurrencyRate() {
    }

    public CurrencyRate(Currency currency, LocalDate date, double exchangeRate) {
        this.currency = currency;
        this.date = date;
        this.exchangeRate = exchangeRate;
    }

    public Long getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
}