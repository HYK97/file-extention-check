package com.flow.fileextensioncheck.service;

import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;

import java.util.Set;

public interface FileExtensionService {
    void save(String fileExtension);

    Set<String> autocomplete(String prefix);

    ResponseFileExtension findAllFileExtension();

    void removeFileExtension(String fileExtension);

    void removeAllFileExtension();
}
