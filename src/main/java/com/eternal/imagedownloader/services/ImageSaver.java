package com.eternal.imagedownloader.services;

import com.eternal.imagedownloader.dto.ImageCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            checkIfImageValid(imageBytes, imageCredentials);
            saveImage(imageBytes, imageCredentials.getFileName());
        } catch (EmptyBodyException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getClass() + " " + e.getMessage());
        } catch (FileIsNotAnImageException e) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getClass() + " " + e.getMessage());
        }
        log.info("Finished saving image {}, {}", imageCredentials.getUrl(), imageCredentials.getFileName());
    }

    private void saveImage(byte[] imageBytes, String fileName) throws IOException {
        File image = new File(imagesDirectoryPath.concat(fileName).concat(".png"));
        InputStream byteInputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(byteInputStream);
        ImageIO.write(bufferedImage, "png", image);
    }

    private void checkIfImageValid(byte[] imageBytes, ImageCredentials imageCredentials) throws IOException {
        String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
        if (!contentType.contains("image")) {
            throw new FileIsNotAnImageException("Downloaded file is not a valid image: " + imageCredentials.getUrl() + " " + imageCredentials.getFileName());
        }
    }
}
