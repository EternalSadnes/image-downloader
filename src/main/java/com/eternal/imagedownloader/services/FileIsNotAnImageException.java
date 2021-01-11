package com.eternal.imagedownloader.services;

import java.io.IOException;

public class FileIsNotAnImageException extends IOException {

    public FileIsNotAnImageException(String message) {
        super(message);
    }
}
