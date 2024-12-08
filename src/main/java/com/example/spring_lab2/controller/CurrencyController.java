package com.example.spring_lab2.controller;

import com.example.spring_lab2.model.CurrencyRate;
import com.example.spring_lab2.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/")
    public String getAllCurrencies(Model model) {
        List<String> currencyNames = currencyService.getAllCurrencyNames();
        model.addAttribute("currencies", currencyNames);
        return "index";
    }

    @GetMapping("/currency/details")
    public String getCurrencyDetails(@RequestParam String name,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     Model model) {
        List<CurrencyRate> rates = currencyService.getRatesByCurrency(name);

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        if (start != null) {
            rates = rates.stream().filter(r -> !r.getDate().isBefore(start)).collect(Collectors.toList());
        }
        if (end != null) {
            rates = rates.stream().filter(r -> !r.getDate().isAfter(end)).collect(Collectors.toList());
        }

        rates = rates.stream().filter(r -> r.getRateChange() != 0).collect(Collectors.toList());

        model.addAttribute("rates", rates);
        model.addAttribute("currencyName", name);
        return "currencyDetails";
    }
}
