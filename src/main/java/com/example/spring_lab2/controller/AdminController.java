package com.example.spring_lab2.controller;

import com.example.spring_lab2.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public String showAdminPage() {
        return "admin/adminHome";
    }

    @GetMapping("/addInitial")
    public String showAddInitialForm() {
        return "admin/addInitialCurrency";
    }

    @PostMapping("/addInitial")
    public String addInitialCurrency(@RequestParam String currencyName,
                                     @RequestParam String date,
                                     @RequestParam double initialRate) {
        currencyService.addInitialCurrency(currencyName, LocalDate.parse(date), initialRate);
        return "redirect:/admin";
    }

    @GetMapping("/addChange")
    public String showAddChangeForm() {
        return "admin/addCurrencyChange";
    }

    @PostMapping("/addChange")
    public String addChange(@RequestParam String currencyName,
                            @RequestParam String date,
                            @RequestParam double rateChange) {
        currencyService.addRateChange(currencyName, LocalDate.parse(date), rateChange);
        return "redirect:/admin";
    }

    @GetMapping("/editName")
    public String showEditNameForm() {
        return "admin/editCurrencyName";
    }

    @PostMapping("/editName")
    public String editCurrencyName(@RequestParam String oldName,
                                   @RequestParam String newName) {
        currencyService.updateCurrencyName(oldName, newName);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String showDeleteForm() {
        return "admin/deleteCurrency";
    }

    @PostMapping("/delete")
    public String deleteCurrency(@RequestParam String currencyName) {
        currencyService.deleteCurrencyByName(currencyName);
        return "redirect:/admin";
    }
}
