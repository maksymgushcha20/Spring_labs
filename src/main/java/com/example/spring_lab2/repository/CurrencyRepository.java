package com.example.spring_lab2.repository;

import com.example.spring_lab2.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCurrencyName(String currencyName);

    @Transactional
    @Modifying
    @Query("UPDATE Currency c SET c.currencyName = :newName WHERE c.currencyName = :oldName")
    int updateCurrencyName(@Param("newName") String newName, @Param("oldName") String oldName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Currency c WHERE c.currencyName = :currencyName")
    void deleteByCurrencyName(@Param("currencyName") String currencyName);

    default List<Currency> getCurrencies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Currency> currencyPage = findAll(pageable);
        return currencyPage.getContent();
    }
}