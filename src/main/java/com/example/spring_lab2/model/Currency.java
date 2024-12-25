package com.example.spring_lab2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_name", nullable = false, unique = true)
    private String currencyName;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "currency", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private List<CurrencyRate> currencyRates = new ArrayList<>();

    public Currency() {
    }

    public Currency(String currencyName, LocalDate createDate) {
        this.currencyName = currencyName;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public List<CurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencyRates(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
    }
}