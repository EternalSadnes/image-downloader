package com.eternal.imagedownloader.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ImageReader {

    @Value("${images.directory.path}")
    private String imagesDirectoryPath;

    public byte[] getOneImage(String imageName) throws IOException {
        File file = new File(imagesDirectoryPath.concat(imageName));
        if (!file.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested image '" + imageName + "' was not found");
        }
        log.info("Found one image: {}", imageName);
        return FileUtils.readFileToByteArray(file);
    }

    public byte[] getAllImagesAsZip() throws IOException {
        log.info("Started creating zip with all images");
        File dir = new File(imagesDirectoryPath);
        File[] imagesArray = dir.listFiles();
        if (imagesArray == null || imagesArray.length == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Images directory is empty or not present");
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bo);
        for (File image : imagesArray) {
            if (!image.isFile()) continue;
            ZipEntry zipEntry = new ZipEntry(image.getName());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(IOUtils.toByteArray(new FileInputStream(image)));
            zipOut.closeEntry();
        }
        zipOut.close();
        log.info("Finished creating zip with all images");
        return bo.toByteArray();
    }
}
