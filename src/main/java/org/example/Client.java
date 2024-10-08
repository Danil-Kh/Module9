package org.example;

import java.io.*;
import java.net.*;

public class Client {
    private static final String END_OF_MESSAGE_MARK = "\n";

    public static void main(String[] args) {
        final int PORT = 8080;

        try (
                Socket socket = new Socket("localhost", PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream()
        ) {
            System.out.println("Підключено до сервера за адресою: http://localhost:" + PORT);

            String messageToSend = "Java" + END_OF_MESSAGE_MARK;
            out.write(messageToSend.getBytes());
            out.flush();

            String response = in.readLine();
            System.out.println("Відповідь від сервера: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

