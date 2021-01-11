package com.eternal.imagedownloader.services;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class ImageDownloader {

    public byte[] downloadImage(String imageUrl) throws IOException {
        log.info("Downloading image from {}", imageUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(imageUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody body = response.body();
        if (isNull(body))
            throw new EmptyBodyException("Response body is empty!");
        return body.bytes();
    }
}
