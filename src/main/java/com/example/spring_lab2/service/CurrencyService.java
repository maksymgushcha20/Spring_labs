package com.example.spring_lab2.service;

import com.example.spring_lab2.model.CurrencyRate;
import com.example.spring_lab2.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.stream.Collectors;
@Service
public class CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public List<CurrencyRate> getAllCurrencyRates() {
        return repository.findAll();
    }

    public List<CurrencyRate> getRatesByCurrency(String currencyName) {
        return repository.findByCurrencyName(currencyName);
    }

    public void addRateChange(String currencyName, LocalDate date, double rateChange) {
        List<CurrencyRate> rates = repository.findByCurrencyName(currencyName);
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
        repository.save(newRecord);
    }

    public void updateCurrencyName(String oldName, String newName) {
        repository.updateCurrencyName(oldName, newName);
    }

    @Transactional
    public void addInitialCurrency(String currencyName, LocalDate date, double initialRate) {
        CurrencyRate initialRecord = new CurrencyRate(currencyName, date, initialRate, initialRate, 0.0);
        repository.save(initialRecord);
    }


    @Transactional
    public boolean deleteCurrencyByName(String name) {
        List<CurrencyRate> deletedCurrency = repository.findByCurrencyName(name);
        repository.deleteByCurrencyName(name);
        return !deletedCurrency.isEmpty();
    }



    @Transactional
    public Optional<CurrencyRate> updateCurrency(Long id, CurrencyRate updatedRate) {
        return repository.findById(id).map(existingRate -> {
            existingRate.setCurrencyName(updatedRate.getCurrencyName());
            existingRate.setDate(updatedRate.getDate());
            existingRate.setExchangeRate(updatedRate.getExchangeRate());
            existingRate.setPreviousRate(updatedRate.getPreviousRate());
            existingRate.setRateChange(updatedRate.getRateChange());
            return repository.save(existingRate);
        });
    }

    @Transactional
    public List<String> getAllCurrencyNames() {
        return repository.findAll().stream()
                .map(CurrencyRate::getCurrencyName)
                .distinct()
                .collect(Collectors.toList());
    }
    @Transactional
    public List<CurrencyRate> getAllCurrencies(int page, int size) {
        return repository.getCurrencies(page, size);
    }
    @Transactional
    public Optional<CurrencyRate> getCurrencyByName(String name) {
        List<CurrencyRate> currencies = repository.findByCurrencyName(name);
        return currencies.isEmpty() ? Optional.empty() : Optional.of(currencies.get(0));
    }
    @Transactional
    public CurrencyRate addCurrency(CurrencyRate currencyRate) {
        repository.save(currencyRate);
        return currencyRate;
    }
}



//package com.example.spring_lab2.service;
//
//import com.example.spring_lab2.model.CurrencyRate;
//import com.example.spring_lab2.repository.CurrencyRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class CurrencyService {
//
//    @Autowired
//    private CurrencyRepository currencyRepository;
//
//    public List<String> getAllCurrencyNames() {
//        return currencyRepository.getAllRates().stream()
//                .map(CurrencyRate::getCurrencyName)
//                .distinct()
//                .collect(Collectors.toList());
//    }
//
//    public List<CurrencyRate> getAllCurrencyRates() {
//        return currencyRepository.getAllRates();
//    }
//
//    public void addInitialCurrency(String currencyName, LocalDate date, double initialRate) {
//        CurrencyRate initialRecord = new CurrencyRate(currencyName, date, initialRate, initialRate, 0.0);
//        currencyRepository.addRate(initialRecord);
//    }
//
//    public void addRateChange(String currencyName, LocalDate date, double rateChange) {
//        List<CurrencyRate> rates = currencyRepository.getRatesByCurrency(currencyName);
//        if (rates.isEmpty()) {
//            return;
//        }
//
//        CurrencyRate lastBefore = rates.stream()
//                .filter(r -> !r.getDate().isAfter(date))
//                .max((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
//                .orElse(null);
//
//        if (lastBefore == null) {
//            return;
//        }
//
//        double newExchangeRate = lastBefore.getExchangeRate() + rateChange;
//        CurrencyRate newRecord = new CurrencyRate(currencyName, date, newExchangeRate, lastBefore.getExchangeRate(), rateChange);
//        currencyRepository.addRate(newRecord);
//    }
//
//    public List<CurrencyRate> getRatesByCurrency(String currencyName) {
//        return currencyRepository.getRatesByCurrency(currencyName);
//    }
//
//    public void updateCurrencyName(String oldName, String newName) {
//        currencyRepository.updateCurrencyName(oldName, newName);
//    }
//
//    public void deleteCurrencyByName(String currencyName) {
//        currencyRepository.deleteByCurrencyName(currencyName);
//    }
//
//    // ======= Додані методи для REST API =======
//
//    public List<CurrencyRate> getAllCurrencies(int page, int size) {
//        return currencyRepository.getCurrencies(page, size);
//    }
//
//    public Optional<CurrencyRate> getCurrencyByName(String name) {
//        return currencyRepository.findByName(name);
//    }
//
//    public CurrencyRate addCurrency(CurrencyRate currencyRate) {
//        currencyRepository.save(currencyRate);
//        return currencyRate;
//    }
//
//    public Optional<CurrencyRate> updateCurrency(String name, CurrencyRate updatedRate) {
//        return currencyRepository.update(name, updatedRate);
//    }
//
//    public boolean deleteCurrency(String name) {
//        if (currencyRepository.currencyExists(name)) {
//            return currencyRepository.delete(name);
//        }
//        return false;
//    }
//
//}
