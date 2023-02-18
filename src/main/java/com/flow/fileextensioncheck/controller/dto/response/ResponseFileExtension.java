package com.flow.fileextensioncheck.controller.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseFileExtension {
    private List<String> fixedExtensions;
    private List<String> customExtensions;

    private ResponseFileExtension(List<String> fixedExtensions, List<String> customExtensions) {
        this.fixedExtensions = fixedExtensions;
        this.customExtensions = customExtensions;
    }

    public static ResponseFileExtension of(List<String> fixedExtensions, List<String> customExtensions) {
        return new ResponseFileExtension(fixedExtensions, customExtensions);
    }
}
