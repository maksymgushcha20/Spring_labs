package com.example.spring_lab2.repository;

import com.example.spring_lab2.model.CurrencyRate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepository {
    private final List<CurrencyRate> currencyRates = new ArrayList<>();

    //  тестові дані
    public CurrencyRepository() {
        currencyRates.add(new CurrencyRate("USD", LocalDate.now(), 1.1, 1.0, 0.1));
        currencyRates.add(new CurrencyRate("EUR", LocalDate.now(), 1.2, 1.1, 0.1));
        currencyRates.add(new CurrencyRate("GBP", LocalDate.now(), 1.3, 1.2, 0.1));
        currencyRates.add(new CurrencyRate("JPY", LocalDate.now(), 110.5, 110.0, 0.5));
        currencyRates.add(new CurrencyRate("CHF", LocalDate.now(), 1.05, 1.04, 0.01));
    }

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

    // ====== Нові методи для REST API ======

    // Пагінація: отримати курси валют з фільтрацією за сторінками
    public List<CurrencyRate> getCurrencies(int page, int size) {
        return currencyRates.stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyRate> findByName(String name) {
        return currencyRates.stream()
                .filter(rate -> rate.getCurrencyName().equalsIgnoreCase(name))
                .findFirst();
    }

    public void save(CurrencyRate rate) {
        currencyRates.add(rate);
    }

    public Optional<CurrencyRate> update(String name, CurrencyRate updatedRate) {
        return findByName(name).map(existing -> {
            existing.setExchangeRate(updatedRate.getExchangeRate());
            existing.setRateChange(updatedRate.getRateChange());
            return existing;
        });
    }

    public boolean delete(String name) {
        boolean isDeleted = currencyRates.removeIf(rate -> rate.getCurrencyName().equalsIgnoreCase(name));
        System.out.println("Attempting to delete currency: " + name + ", Success: " + isDeleted);
        return isDeleted;
    }

}
