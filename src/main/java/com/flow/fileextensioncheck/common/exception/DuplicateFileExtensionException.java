package com.flow.fileextensioncheck.common.exception;

public class DuplicateFileExtensionException extends RuntimeException {


    public DuplicateFileExtensionException(String fileExtension, String message) {
        super(message + " : " + fileExtension);
    }

    public DuplicateFileExtensionException(String fileExtension, String message, Throwable cause) {
        super(message + " : " + fileExtension, cause);
    }


}
