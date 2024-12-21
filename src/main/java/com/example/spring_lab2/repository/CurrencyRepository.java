package com.example.spring_lab2.repository;

import com.example.spring_lab2.model.CurrencyRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<CurrencyRate, Long> {

    List<CurrencyRate> findByCurrencyName(String currencyName);

    @Transactional
    @Modifying
    @Query("UPDATE CurrencyRate c SET c.currencyName = :newName WHERE c.currencyName = :oldName")
    void updateCurrencyName(String oldName, String newName);

    @Transactional
    @Modifying
    @Query("DELETE FROM CurrencyRate c WHERE c.currencyName = :currencyName")
    void deleteByCurrencyName(String currencyName);

    default List<CurrencyRate> getCurrencies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CurrencyRate> currencyPage = findAll(pageable);
        return currencyPage.getContent();
    }
}

