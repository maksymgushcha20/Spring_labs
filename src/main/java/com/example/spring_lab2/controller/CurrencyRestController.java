package com.example.spring_lab2.controller;

import com.example.spring_lab2.dto.CurrencyRateDTO;
import com.example.spring_lab2.dto.CurrencyFullDTO;
import com.example.spring_lab2.dto.CurrencyUpdateDTO;
import com.example.spring_lab2.model.Currency;
import com.example.spring_lab2.model.CurrencyRate;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
            content = @Content(schema = @Schema(implementation = CurrencyFullDTO.class))),
        @ApiResponse(responseCode = "404", description = "Валюта не знайдена"),
        @ApiResponse(responseCode = "500", description = "Назва валюти")
    })
    @GetMapping("/{currencyName}")
    public ResponseEntity<CurrencyFullDTO> getCurrencyDetails(
            @Parameter(description = "Name of the currency to retrieve") 
            @PathVariable("currencyName") String currencyName
    ) {
    Optional<Currency> currencyOpt = currencyService.getCurrencyByName(currencyName);
        if (currencyOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
        }
        Currency currency = currencyOpt.get();
        
        List<CurrencyRate> currencyRates = currencyService.getRatesByCurrency(currencyName)
                .stream()
                .sorted(Comparator.comparing(CurrencyRate::getDate))
                .collect(Collectors.toList());
        
        AtomicReference<Double> previousRate = new AtomicReference<>(null);
        
        List<CurrencyRateDTO> rates = currencyRates.stream()
                .map(r -> {
                        Double prevRate = previousRate.get();
                        double rateChange = (prevRate != null) ? r.getExchangeRate() - prevRate : 0;
                        double percentageChange = (prevRate != null && prevRate != 0) ? (rateChange / prevRate) * 100 : 0;
                        previousRate.set(r.getExchangeRate());
                        return new CurrencyRateDTO(r.getDate(), r.getExchangeRate(), rateChange, percentageChange);
                })
                .collect(Collectors.toList());
        
        CurrencyFullDTO dto = new CurrencyFullDTO(
                currency.getId(),
                currency.getCurrencyName(),
                currency.getCreateDate(),
                rates
        );
        return ResponseEntity.ok(dto);
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

    @PutMapping
    @Operation(
        summary = "Оновити інформацію про валюту",
        description = "Оновлює дані валюти за назвою. Якщо дата існує, оновлює запис, інакше додає новий. Якщо ім'я валюти не існує, повертає помилку."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успішно оновлено"),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту"),
        @ApiResponse(responseCode = "404", description = "Валюта не знайдена")
    })

    public ResponseEntity<?> updateCurrency(
            @RequestBody CurrencyUpdateDTO currencyUpdateDTO) {
        Optional<Currency> currencyOpt = currencyService.getCurrencyByName(currencyUpdateDTO.getCurrencyName());
        if (!currencyOpt.isPresent()) {
            return ResponseEntity.status(404).body("Валюта не знайдена");
        }
    
        Currency currency = currencyOpt.get();
    
        boolean updated = currencyService.updateOrAddRate(currency, currencyUpdateDTO.getDate(), currencyUpdateDTO.getInitialRate());
        if (updated) {
            return ResponseEntity.ok("Дані успішно оновлено");
        } else {
            return ResponseEntity.status(500).body("Помилка при оновленні даних");
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