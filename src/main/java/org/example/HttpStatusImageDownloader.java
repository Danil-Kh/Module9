package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpStatusImageDownloader {
    HttpStatusChecker httpStatusChecker = new HttpStatusChecker();
    public void downloadStatusImage(String code) throws IOException, URISyntaxException {
        String url = httpStatusChecker.getResponse(code);
        URL urlObj = new URL(url);
        InputStream inputStream = urlObj.openStream();
           if(!Files.exists(Path.of("C:\\Java programs\\module9_1\\src\\main\\resources\\image\\"+code+".jpg"))){
               Files.copy(inputStream, new File("C:\\Java programs\\module9_1\\src\\main\\resources\\image\\"+code+".jpg").toPath());
           }
    }
}
