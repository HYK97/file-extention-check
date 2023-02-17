package com.flow.fileextensioncheck.service;

import com.flow.fileextensioncheck.controller.dto.response.ResponseFileExtension;

public interface FileExtensionService {
    void save(String fileExtension);

    ResponseFileExtension findAllFileExtension();

    void removeFileExtension(String fileExtension);

    void removeAllFileExtension();
}
