package com.example.spring_lab2.controller;

import com.example.spring_lab2.model.Currency;
import com.example.spring_lab2.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyRestController {

    @Autowired
    private CurrencyService currencyService;

    @Operation(
            summary = "Отримати всі валюти",
            description = "Повертає список валют з підтримкою пагінації. За замовчуванням використовується перша сторінка з 10 записами."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успішна відповідь",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies(
            @Parameter(description = "Номер сторінки", example = "0")
            @RequestParam Optional<Integer> page,
            @Parameter(description = "Розмір сторінки", example = "10")
            @RequestParam Optional<Integer> size) {
        List<Currency> currencies = currencyService.getAllCurrencies(
                page.orElse(0), 
                size.orElse(10)
        );
        return ResponseEntity.ok(currencies);
    }

    @Operation(
            summary = "Отримати валюту за ім'ям",
            description = "Повертає інформацію про валюту на основі її назви."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Валюта знайдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена",
                    content = @Content)
    })
    @GetMapping("/{name}")
    public ResponseEntity<Currency> getCurrencyByName(
            @Parameter(description = "Назва валюти", required = true, example = "USD")
            @PathVariable String name) {
        return currencyService.getCurrencyByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Створити нову валюту",
            description = "Додає нову валюту до системи. Автоматично встановлює дату створення."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Валюта успішно створена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані або валюта вже існує",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> addCurrency(
            @Parameter(description = "Дані нової валюти", required = true,
                       schema = @Schema(implementation = Currency.class))
            @RequestBody Currency currency) {
        try {
            Currency createdCurrency = currencyService.addCurrency(
                    currency.getCurrencyName(), 
                    LocalDate.now()
            );
            return ResponseEntity.status(201).body(createdCurrency);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Валюта з такою назвою вже існує.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Внутрішня помилка сервера.");
        }
    }

    @Operation(
            summary = "Оновити валюту",
            description = "Оновлює інформацію про валюту на основі її ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Валюта успішно оновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurrency(
            @Parameter(description = "ID валюти, яку потрібно оновити", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Оновлені дані валюти", required = true,
                       schema = @Schema(implementation = Currency.class))
            @RequestBody Currency updatedCurrency) {
        try {
            Optional<Currency> currencyOpt = currencyService.updateCurrency(id, updatedCurrency);
            return currencyOpt
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(404).build());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Некоректні дані для оновлення валюти.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Внутрішня помилка сервера.");
        }
    }

    @Operation(
            summary = "Видалити валюту за ім'ям",
            description = "Видаляє валюту з системи за її назвою."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Валюта успішно видалена",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content)
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCurrency(
            @Parameter(description = "Назва валюти для видалення", required = true, example = "USD")
            @PathVariable String name) {
        try {
            currencyService.deleteCurrencyByName(name);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Валюта не знайдена.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Внутрішня помилка сервера.");
        }
    }
}