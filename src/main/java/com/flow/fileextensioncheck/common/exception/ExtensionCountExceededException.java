package com.flow.fileextensioncheck.common.exception;

public class ExtensionCountExceededException extends RuntimeException {


    public ExtensionCountExceededException(int maxCount, String message) {
        super(message + " : " + maxCount);
    }

    public ExtensionCountExceededException(int maxCount, String message, Throwable cause) {
        super(message + " : " + maxCount, cause);
    }

}
