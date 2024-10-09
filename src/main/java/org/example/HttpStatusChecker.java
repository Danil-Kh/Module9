package org.example;

import java.io.IOException;
import java.net.*;

public class HttpStatusChecker {
    private String response;
    private String getStatusImage(String inputText) throws IncorrectCodeExeptions {
        try {

            URI uri = new URI("https://http.cat/status/" + inputText);

            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                response = "https://http.cat/"+inputText+".jpg";
            }
            else {
                throw new IncorrectCodeExeptions("Error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IncorrectCodeExeptions e) {
            throw new  IncorrectCodeExeptions("Error");
        }
        return response;
    }
    public String getResponse(String inputText) throws IncorrectCodeExeptions {
        return getStatusImage(inputText);
    }
}
