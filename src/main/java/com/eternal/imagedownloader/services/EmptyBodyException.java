package com.eternal.imagedownloader.services;

import java.io.IOException;

public class EmptyBodyException extends IOException {

    public EmptyBodyException(String message) {
        super(message);
    }
}
