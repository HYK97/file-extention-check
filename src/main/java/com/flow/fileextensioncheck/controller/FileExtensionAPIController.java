package com.flow.fileextensioncheck.controller;

import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;
import com.flow.fileextensioncheck.service.FileExtensionService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extension")
@RequiredArgsConstructor
@Validated
public class FileExtensionAPIController {
    private final FileExtensionService fileExtensionService;

    @PostMapping
    public ResponseEntity<Void> addFileExtension(@NotEmpty @Length(max = 20) String fileExtension) {
        fileExtensionService.save(fileExtension);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<ResponseFileExtension> initData() {
        return ResponseEntity.ok(fileExtensionService.findAllFileExtension());
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFileExtension(@NotEmpty String fileExtension) {
        fileExtensionService.removeFileExtension(fileExtension);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/all")
    public ResponseEntity<Void> removeAllFileExtension() {
        fileExtensionService.removeAllFileExtension();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
