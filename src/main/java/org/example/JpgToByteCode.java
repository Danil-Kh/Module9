package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JpgToByteCode {
    private final HttpStatusImageDownloader httpStatusImageDownloader = new HttpStatusImageDownloader();
    private byte[] imageByArray;
    public byte[] byteConvert(String code) throws IOException, URISyntaxException, IncorrectCodeExeptions {

        if (!Files.exists(Path.of("C:\\Java programs\\module9_1\\src\\main\\resources\\image\\"+code+".jpg"))){
            httpStatusImageDownloader.downloadStatusImage(code);
            imageByArray = Files.readAllBytes(Paths.get("C:\\Java programs\\module9_1\\src\\main\\resources\\image\\"+code+".jpg"));
        }
        else {
            imageByArray = Files.readAllBytes(Paths.get("C:\\Java programs\\module9_1\\src\\main\\resources\\image\\"+code+".jpg"));
        }
        return imageByArray;
    }
}
