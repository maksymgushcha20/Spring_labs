package com.example.spring_lab2.controller;

import com.example.spring_lab2.model.CurrencyRate;
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
                            schema = @Schema(implementation = CurrencyRate.class)))
    })
    @GetMapping
    public ResponseEntity<List<CurrencyRate>> getAllCurrencies(
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyRate.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })
    @GetMapping("/{name}")
    public ResponseEntity<CurrencyRate> getCurrencyByName(
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyRate.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані для створення валюти")
    })
    @PostMapping
    public ResponseEntity<CurrencyRate> addCurrency(
            @Parameter(description = "Дані нової валюти", required = true)
            @RequestBody CurrencyRate currencyRate) {
        return ResponseEntity.status(201).body(currencyService.addCurrency(currencyRate));
    }

    @Operation(
            summary = "Оновити валюту",
            description = "Оновлює інформацію про валюту на основі її назви."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Валюта успішно оновлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyRate.class))),
            @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyRate> updateCurrency(
            @Parameter(description = "ID валюти, яку потрібно оновити", required = true)
            @PathVariable Long id,
            @Parameter(description = "Оновлені дані валюти", required = true)
            @RequestBody CurrencyRate updatedRate) {
        return currencyService.updateCurrency(id, updatedRate)
                .map(ResponseEntity::ok)
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
        if (currencyService.deleteCurrencyByName(name)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
