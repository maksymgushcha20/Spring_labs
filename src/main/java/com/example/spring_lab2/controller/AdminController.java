package com.example.spring_lab2.controller;

import com.example.spring_lab2.service.CurrencyService;
import com.example.spring_lab2.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                                    @RequestParam double initialRate,
                                    Model model) {
        try {
            Currency savedCurrency = currencyService.addCurrency(currencyName, LocalDate.now());
            currencyService.addCurrencyRate(savedCurrency.getId(), LocalDate.parse(date), initialRate);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/addInitialCurrency";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "A database error occurred: " + e.getRootCause().getMessage());
            return "admin/addInitialCurrency";
        }
    }


    @GetMapping("/addChange")
    public String showAddChangeForm() {
        return "admin/addCurrencyChange";
    }

    // Long currencyId, LocalDate date, double exchangeRate
    @PostMapping("/addChange")
    public String addChange(@RequestParam String currencyName,
                            @RequestParam LocalDate date,
                            @RequestParam double exchangeRate, 
                            Model model) {
        try {
            Long currencyId = currencyService.getCurrencyIdByName(currencyName);
            currencyService.addCurrencyRate(currencyId, date, exchangeRate);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/addCurrencyChange";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "A database error occurred: " + e.getRootCause().getMessage());
            return "admin/addCurrencyChange";
        }
    }

    @GetMapping("/editName")
    public String showEditNameForm() {
        return "admin/editCurrencyName";
    }

    @PostMapping("/editName")
    public String editCurrencyName(@RequestParam Long currencyId,
                                   @RequestParam String newName,
                                   Model model) {
        try {
            currencyService.updateCurrencyName(currencyId, newName);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/editCurrencyName";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "A database error occurred: " + e.getRootCause().getMessage());
            return "admin/editCurrencyName";
        }
    }

    @GetMapping("/delete")
    public String showDeleteForm() {
        return "admin/deleteCurrency";
    }

    @PostMapping("/delete")
    public String deleteCurrency(@RequestParam Long currencyId, Model model) {
        try {
            currencyService.deleteCurrencyById(currencyId);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting currency: " + e.getMessage());
            return "admin/deleteCurrency";
        }
    }
}