package com.example.spring_lab2.repository;

import com.example.spring_lab2.model.Currency;
import com.example.spring_lab2.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    @Query("SELECT cr FROM CurrencyRate cr WHERE cr.currency.currencyName = :currencyName")
    List<CurrencyRate> findByCurrencyName(@Param("currencyName") String currencyName);
    List<CurrencyRate> findByCurrency_CurrencyName(String currencyName);

    List<CurrencyRate> findByCurrency_CurrencyNameOrderByDateAsc(String currencyName);
    Optional<CurrencyRate> findByCurrencyAndDate(Currency currency, LocalDate date);

    
    @Modifying
    @Transactional
    @Query("UPDATE CurrencyRate cr SET cr.exchangeRate = :newRate WHERE cr.currency.id = :currencyId AND cr.date = :date")
    int updateExchangeRateByCurrencyAndDate(@Param("newRate") double newRate, @Param("currencyId") Long currencyId, @Param("date") LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM CurrencyRate cr WHERE cr.currency.id = :currencyId AND cr.date = :date")
    void deleteByCurrencyAndDate(@Param("currencyId") Long currencyId, @Param("date") LocalDate date);
    
    Optional<CurrencyRate> findTopByCurrencyIdAndDateBeforeOrderByDateDesc(@Param("currencyId") Long currencyId, @Param("date") LocalDate date);
}