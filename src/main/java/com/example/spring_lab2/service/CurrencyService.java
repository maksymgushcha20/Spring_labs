package com.example.spring_lab2.service;

import com.example.spring_lab2.model.CurrencyRate;
import com.example.spring_lab2.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<String> getAllCurrencyNames() {
        return currencyRepository.getAllRates().stream()
                .map(CurrencyRate::getCurrencyName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<CurrencyRate> getAllCurrencyRates() {
        return currencyRepository.getAllRates();
    }

    public void addInitialCurrency(String currencyName, LocalDate date, double initialRate) {
        CurrencyRate initialRecord = new CurrencyRate(currencyName, date, initialRate, initialRate, 0.0);
        currencyRepository.addRate(initialRecord);
    }

    public void addRateChange(String currencyName, LocalDate date, double rateChange) {
        List<CurrencyRate> rates = currencyRepository.getRatesByCurrency(currencyName);
        if (rates.isEmpty()) {
            return;
        }

        CurrencyRate lastBefore = rates.stream()
                .filter(r -> !r.getDate().isAfter(date))
                .max((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .orElse(null);

        if (lastBefore == null) {
            return;
        }

        double newExchangeRate = lastBefore.getExchangeRate() + rateChange;
        CurrencyRate newRecord = new CurrencyRate(currencyName, date, newExchangeRate, lastBefore.getExchangeRate(), rateChange);
        currencyRepository.addRate(newRecord);
    }

    public List<CurrencyRate> getRatesByCurrency(String currencyName) {
        return currencyRepository.getRatesByCurrency(currencyName);
    }

    public void updateCurrencyName(String oldName, String newName) {
        currencyRepository.updateCurrencyName(oldName, newName);
    }

    public void deleteCurrencyByName(String currencyName) {
        currencyRepository.deleteByCurrencyName(currencyName);
    }

    // ======= Додані методи для REST API =======

    public List<CurrencyRate> getAllCurrencies(int page, int size) {
        return currencyRepository.getCurrencies(page, size);
    }

    public Optional<CurrencyRate> getCurrencyByName(String name) {
        return currencyRepository.findByName(name);
    }

    public CurrencyRate addCurrency(CurrencyRate currencyRate) {
        currencyRepository.save(currencyRate);
        return currencyRate;
    }

    public Optional<CurrencyRate> updateCurrency(String name, CurrencyRate updatedRate) {
        return currencyRepository.update(name, updatedRate);
    }

    public boolean deleteCurrency(String name) {
        if (currencyRepository.currencyExists(name)) {
            return currencyRepository.delete(name);
        }
        return false;
    }

}
