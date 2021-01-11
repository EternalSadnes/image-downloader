package com.eternal.imagedownloader.services;

import com.eternal.imagedownloader.dto.ImageCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageSaver {

    @Value("${images.directory.path}")
    private String imagesDirectoryPath;
    private final ImageDownloader imageDownloader;

    public void saveImage(ImageCredentials imageCredentials) throws IOException {
        log.info("Started saving image {}, {}", imageCredentials.getUrl(), imageCredentials.getFileName());
        try {
            byte[] imageBytes = imageDownloader.downloadImage(imageCredentials.getUrl());
            FileUtils.writeByteArrayToFile(new File(String.format(imagesDirectoryPath.concat("%s"), imageCredentials.getFileName())), imageBytes);
            checkIfImageValid(imageBytes, imageCredentials);
        } catch (EmptyBodyException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getClass() + " " + e.getMessage());
        } catch (FileIsNotAnImageException e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getClass() + " " + e.getMessage());
        }
        log.info("Finished saving image {}, {}", imageCredentials.getUrl(), imageCredentials.getFileName());
    }

    private void checkIfImageValid(byte[] imageBytes, ImageCredentials imageCredentials) throws IOException {
        String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
        if (!contentType.contains("image")) {
            throw new FileIsNotAnImageException("Downloaded file is not a valid image: " + imageCredentials.getUrl() + " " + imageCredentials.getFileName());
        }
    }
}
