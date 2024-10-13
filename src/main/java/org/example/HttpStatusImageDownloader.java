package org.example;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class HttpStatusImageDownloader {

    public void downloadStatusImage(String code, String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        Path imagePath = Paths.get("src/main/resources/image/" + code + ".jpg");

        if (!Files.exists(imagePath)) {
            try (InputStream inputStream = url.openStream()) {
                Files.createDirectories(imagePath.getParent());
                Files.copy(inputStream, imagePath);
                System.out.println("Image downloaded to: " + imagePath);
            }
        } else {
            System.out.println("Image already exists at: " + imagePath);
        }
    }
}
