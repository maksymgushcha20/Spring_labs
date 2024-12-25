package com.example.spring_lab2.service;

import com.example.spring_lab2.model.Currency;
import com.example.spring_lab2.model.CurrencyRate;
import com.example.spring_lab2.repository.CurrencyRepository;
import com.example.spring_lab2.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyRateRepository currencyRateRepository; 
    
    @Transactional
    public Long getCurrencyIdByName(String currencyName) {    
        Currency currency = currencyRepository.findByCurrencyName(currencyName)
            .orElseThrow(() -> new IllegalArgumentException("Currency not found"));    
        return currency.getId();    
    }

    @Transactional
    public Currency addCurrency(String currencyName, LocalDate createDate) {
        // Check if currency already exists
        if (currencyRepository.findByCurrencyName(currencyName).isPresent()) {
            throw new IllegalArgumentException("Currency with name '" + currencyName + "' already exists.");
        }
        Currency currency = new Currency(currencyName, createDate);
        return currencyRepository.save(currency);
    }

    @Transactional
    public Optional<Currency> getCurrencyByName(String currencyName) {
        return currencyRepository.findByCurrencyName(currencyName);
    }

    @Transactional
    public List<Currency> getAllCurrencies(int page, int size) {
        return currencyRepository.getCurrencies(page, size);
    }

    @Transactional
    public Optional<Currency> updateCurrency(Long id, Currency updatedCurrency) {
        return currencyRepository.findById(id).map(currency -> {
            currency.setCurrencyName(updatedCurrency.getCurrencyName());
            currency.setCreateDate(updatedCurrency.getCreateDate());
            return currencyRepository.save(currency);
        });
    }

    @Transactional
    public boolean deleteCurrencyByName(String name) {
        Optional<Currency> currencyOpt = currencyRepository.findByCurrencyName(name);
        if (currencyOpt.isPresent()) {
            currencyRepository.delete(currencyOpt.get());
            return true;
        }
        return false;
    }

    @Transactional
    public CurrencyRate addCurrencyRate(Long currencyId, LocalDate date, double exchangeRate) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency ID"));
        CurrencyRate rate = new CurrencyRate(currency, date, exchangeRate);
        return currencyRateRepository.save(rate);
    }

    @Transactional
    public void updateCurrencyName(Long currencyId, String newName) {
        // Check if new currency name already exists
        if (currencyRepository.findByCurrencyName(newName).isPresent()) {
            throw new IllegalArgumentException("Currency with name '" + newName + "' already exists.");
        }

        currencyRepository.findById(currencyId).ifPresent(currency -> {
            currency.setCurrencyName(newName);
            currencyRepository.save(currency);
        });
    }

    @Transactional
    public void deleteCurrencyById(Long currencyId) {
        currencyRepository.deleteById(currencyId);
    }

    @Transactional
    public List<String> getAllCurrencyNames() {
        return currencyRepository.findAll()
                .stream()
                .map(Currency::getCurrencyName)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CurrencyRate> getRatesByCurrency(String name) {
        return currencyRateRepository.findByCurrencyName(name);
    }

    @Transactional
    public CurrencyRate createNewCurrencyRate(Long currencyId, LocalDate date, double newExchangeRate) {
        // Fetch the last CurrencyRate before the given date
        Optional<CurrencyRate> lastBeforeOpt = currencyRateRepository.findTopByCurrencyIdAndDateBeforeOrderByDateDesc(currencyId, date);
        double previousRate = lastBeforeOpt.map(CurrencyRate::getExchangeRate).orElse(0.0);
        double rateChange = newExchangeRate - previousRate;

        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid currency ID"));

        CurrencyRate newRecord = new CurrencyRate(currency, date, newExchangeRate);
        // Handle rateChange if necessary (e.g., logging)
        return currencyRateRepository.save(newRecord);
    }
}