package com.flow.fileextensioncheck.controller;

import com.flow.fileextensioncheck.service.FileExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/autocomplete")
@RequiredArgsConstructor
public class AutocompleteAPIController {
    private final FileExtensionService fileExtensionService;

    @GetMapping
    public ResponseEntity<Set<String>> autocomplete(String word) {
        return ResponseEntity.ok(fileExtensionService.autocomplete(word));
    }
}
