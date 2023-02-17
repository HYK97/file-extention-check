package com.flow.fileextensioncheck.controller.dto.response;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ResponseFileExtension {
    private final List<String> fixedExtensions;
    private final List<String> customExtensions;

    private ResponseFileExtension(List<String> fixedExtensions, List<String> customExtensions) {
        this.fixedExtensions = fixedExtensions;
        this.customExtensions = customExtensions;
    }

    public static ResponseFileExtension of(List<String> fixedExtensions, List<String> customExtensions) {
        return new ResponseFileExtension(fixedExtensions, customExtensions);
    }
}
