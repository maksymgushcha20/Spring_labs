package com.example.spring_lab2.repository;

import com.example.spring_lab2.model.CurrencyRate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepository {
    private final List<CurrencyRate> currencyRates = new ArrayList<>();

    public List<CurrencyRate> getAllRates() {
        return new ArrayList<>(currencyRates);
    }

    public void addRate(CurrencyRate rate) {
        currencyRates.add(rate);
    }

    public List<CurrencyRate> getRatesByCurrency(String currencyName) {
        return currencyRates.stream()
                .filter(r -> r.getCurrencyName().equalsIgnoreCase(currencyName))
                .sorted(Comparator.comparing(CurrencyRate::getDate))
                .collect(Collectors.toList());
    }

    public boolean currencyExists(String currencyName) {
        return currencyRates.stream()
                .anyMatch(r -> r.getCurrencyName().equalsIgnoreCase(currencyName));
    }

    public void updateCurrencyName(String oldName, String newName) {
        for (CurrencyRate rate : currencyRates) {
            if (rate.getCurrencyName().equalsIgnoreCase(oldName)) {
                rate.setCurrencyName(newName);
            }
        }
    }

    public void deleteByCurrencyName(String currencyName) {
        currencyRates.removeIf(r -> r.getCurrencyName().equalsIgnoreCase(currencyName));
    }
}
