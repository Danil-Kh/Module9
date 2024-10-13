package org.example;

import java.io.IOException;
import java.net.*;

public class HttpStatusChecker {
    private HttpStatusImageDownloader httpStatusImageDownloader = new HttpStatusImageDownloader();

    public String getResponse(String inputText) throws IncorrectCodeExeptions {
        return getStatusImage(inputText);
    }

    private String getStatusImage(String inputText) throws IncorrectCodeExeptions {
        try {
            URL url = new URL("https://http.cat/status/" + inputText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String imageUrl = "https://http.cat/" + inputText + ".jpg";
                httpStatusImageDownloader.downloadStatusImage(inputText, imageUrl);
                return imageUrl;
            } else {
                throw new IncorrectCodeExeptions("Invalid status code: " + inputText);
            }
        } catch (IOException URISyntaxException) {
            throw new RuntimeException("Error processing request");
        }

    }
}
