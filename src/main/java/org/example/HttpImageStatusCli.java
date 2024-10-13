package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class HttpImageStatusCli {
    private static HttpStatusChecker httpStatusChecker = new HttpStatusChecker();
    private static int code;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final int PORT = 8080;

        starterServer(PORT);
    }

    private static void starterServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started at port " + port);
            while (true) {
                handleClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient() throws IOException {
        System.out.print("Input HTTP status code: ");
        String inputText = scanner.nextLine();

        try {
            code = Integer.parseInt(inputText);
            String response = httpStatusChecker.getResponse(inputText);
            System.out.println("Image URL: " + response);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
        } catch (IncorrectCodeExeptions e) {
            System.out.println("There is no image for HTTP status <" + inputText + ">");
        }
    }
}
