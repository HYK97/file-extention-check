package com.flow.fileextensioncheck.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/")
public class FileExtensionViewController {
    @Value("${fixed-extension.list}")
    private List<String> allFixedExtensions;

    @GetMapping
    public String initView(Model model) {
        model.addAttribute("fixedExtensions", allFixedExtensions);
        return "index";
    }
}
