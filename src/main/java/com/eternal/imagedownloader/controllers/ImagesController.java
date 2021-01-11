package com.eternal.imagedownloader.controllers;

import com.eternal.imagedownloader.dto.ImageCredentials;
import com.eternal.imagedownloader.services.ImageReader;
import com.eternal.imagedownloader.services.ImageSaver;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImagesController {

    private final ImageSaver imageSaver;
    private final ImageReader imageReader;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveImage(@RequestBody ImageCredentials imageCredentials) throws IOException {
        imageSaver.saveImage(imageCredentials);
    }

    @GetMapping(value = "/show/one",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] showOneImage(@RequestParam String imageName) throws IOException {
        return imageReader.getOneImage(imageName);
    }

    @GetMapping(value = "/get/one")
    public HttpEntity<byte[]> getOneImage(@RequestParam String imageName) throws IOException {
        byte[] imageBytes = imageReader.getOneImage(imageName);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + imageName);
        header.setContentLength(imageBytes.length);
        return new HttpEntity<>(imageBytes, header);
    }

    @GetMapping("/get/all")
    public HttpEntity<byte[]> getAllImagesAsZip() throws IOException {
        byte[] allImagesAsZip = imageReader.getAllImagesAsZip();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + "AllImages.zip");
        header.setContentLength(allImagesAsZip.length);
        return new HttpEntity<>(allImagesAsZip, header);
    }
}
