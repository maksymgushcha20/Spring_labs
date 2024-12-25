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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                            schema = @Schema(implementation = Currency.class)))
    })
    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies(
            @Parameter(description = "Номер сторінки (починається з 0)", example = "0")
            @RequestParam Optional<Integer> page,
            @Parameter(description = "Кількість записів на сторінці", example = "10")
            @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(currencyService.getAllCurrencies(page.orElse(0), size.orElse(10)));
    }

    @Operation(
            summary = "Отримати валюту за ім'ям",
            description = "Повертає інформацію про валюту на основі її назви."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Валюта знайдена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })
    @GetMapping("/{name}")
    public ResponseEntity<Currency> getCurrencyByName(
            @Parameter(description = "Назва валюти для пошуку", required = true)
            @PathVariable String name) {
        return currencyService.getCurrencyByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Створити нову валюту",
            description = "Додає нову валюту до системи."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Валюта успішно створена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для створення валюти")
    })
    @PostMapping
    public ResponseEntity<Currency> addCurrency(
            @Parameter(description = "Дані нової валюти", required = true)
            @RequestBody Currency currency) {
        Currency createdCurrency = currencyService.addCurrency(currency.getCurrencyName(), currency.getCreateDate());
        return ResponseEntity.status(201).body(createdCurrency);
    }

    @Operation(
            summary = "Оновити валюту",
            description = "Оновлює інформацію про валюту на основі її назви."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Валюта успішно оновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Currency.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(
            @Parameter(description = "ID валюти, яку потрібно оновити", required = true)
            @PathVariable Long id,
            @Parameter(description = "Оновлені дані валюти", required = true)
            @RequestBody Currency updatedCurrency) {
        Optional<Currency> currencyOpt = currencyService.updateCurrency(id, updatedCurrency);
        return currencyOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Видалити валюту за ім'ям",
            description = "Видаляє валюту з системи за її назвою."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Валюта успішно видалена"),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCurrency(
            @Parameter(description = "Назва валюти для видалення", required = true)
            @PathVariable String name) {
        boolean deleted = currencyService.deleteCurrencyByName(name);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}