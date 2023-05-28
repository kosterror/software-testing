package ru.tsu.hits.kosterror.palindromepartitioning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tsu.hits.kosterror.palindromepartitioning.exception.IncorrectInputStringException;
import ru.tsu.hits.kosterror.palindromepartitioning.service.PalindromePartitioningService;

@Controller
@RequestMapping
public class PalindromePartitioningController {

    private final PalindromePartitioningService service;

    public PalindromePartitioningController(PalindromePartitioningService service) {
        this.service = service;
    }

    @GetMapping
    public String homePage() {
        return "input_page";
    }

    @GetMapping("/all-possible-palindromes")
    public String getAllPossiblePalindromes(
            @RequestParam("input") String inputString,
            Model model
    ) throws IncorrectInputStringException {
        model.addAttribute("result", service.getAllPossiblePalindromes(inputString));
        return "result_page";
    }

}
