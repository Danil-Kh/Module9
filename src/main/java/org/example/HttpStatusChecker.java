package org.example;

import java.io.IOException;
import java.net.*;

public class HttpStatusChecker {
    private String response;
    private String getStatusImage(String inputText)  {
        try {
            String scheme = "https";
            String host = "http.cat";
            String path = "/status/" + inputText;

            URI uri = new URI(scheme, host, path, null);

            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                response = "https://http.cat/"+inputText+".jpg";
            }
            else {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    public String getResponse(String inputText)  {
        return getStatusImage(inputText);
    }
}
