package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpImageStatusCli {
    private static final String END_OF_MESSAGE_MARK = "\n";
    private static ExecutorService pool;
    private static ServerSocket serverSocket;
    private static HttpStatusChecker httpStatusChecker = new HttpStatusChecker();
    private static HttpStatusImageDownloader httpStatusImageDownloader = new HttpStatusImageDownloader();
    private static JpgToByteCode jpgToByteCode = new JpgToByteCode();
    private static int code;

    public static void main(String[] args) {
        final int MAX_CONNECTIONS = 10;
        final int PORT = 8080;


        starterServer(MAX_CONNECTIONS, PORT);


        runClientOnServer("localhost", PORT);
    }

    private static void starterServer(int maxConnections, int port) {
        pool = Executors.newFixedThreadPool(maxConnections);

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);


            while (true) {
                listenForClients(serverSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listenForClients(ServerSocket serverSocket) {
        try {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            pool.submit(() -> handleClient(clientSocket));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();

            if (requestLine != null && requestLine.startsWith("GET / ")) {

                String htmlResponse = getHTMLResponse();
                String serverResponse = "HTTP/1.1 200 OK" + END_OF_MESSAGE_MARK +
                        "Content-Type: text/html" + END_OF_MESSAGE_MARK +
                        "Content-Length: " + htmlResponse.length() + END_OF_MESSAGE_MARK +
                        END_OF_MESSAGE_MARK +
                        htmlResponse;

                out.write(serverResponse.getBytes());
                out.flush();

            } else if (requestLine != null && requestLine.startsWith("POST / ")) {

                String line;
                int contentLength = 0;
                while (!(line = in.readLine()).isEmpty()) {

                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                char[] body = new char[contentLength];
                in.read(body);
                String requestBody = new String(body);


                String[] params = requestBody.split("&");
                String inputText = "";
                for (String param : params) {
                    if (param.startsWith("inputText=")) {
                        inputText = param.split("=")[1];
                        inputText = URLDecoder.decode(inputText, "UTF-8");
                        break;
                    }
                }
                try{
                   code = Integer.parseInt(inputText);
                   httpStatusChecker.getResponse(inputText);
                }catch (NumberFormatException e){
                    String htmlResponse = "Please enter valid number";
                    String serverResponse = "HTTP/1.1 200 OK" + END_OF_MESSAGE_MARK +
                            "Content-Type: text/html" + END_OF_MESSAGE_MARK +
                            "Content-Length: " + htmlResponse.length() + END_OF_MESSAGE_MARK +
                            END_OF_MESSAGE_MARK +
                            htmlResponse;
                    out.write(serverResponse.getBytes());
                    out.flush();
                }
                catch (RuntimeException e){
                    String htmlResponse = "There is not image for HTTP status <CODE>";
                    String serverResponse = "HTTP/1.1 200 OK" + END_OF_MESSAGE_MARK +
                            "Content-Type: text/html" + END_OF_MESSAGE_MARK +
                            "Content-Length: " + htmlResponse.length() + END_OF_MESSAGE_MARK +
                            END_OF_MESSAGE_MARK +
                            htmlResponse;
                    out.write(serverResponse.getBytes());
                    out.flush();
                }



                byte[] imageByArray = jpgToByteCode.byteConvert(inputText);


                String serverResponse = "HTTP/1.1 200 OK" + END_OF_MESSAGE_MARK +
                        "Content-Type: image/jpeg" + END_OF_MESSAGE_MARK +
                        "Content-Length: " + imageByArray.length + END_OF_MESSAGE_MARK +
                        END_OF_MESSAGE_MARK;
                out.write(serverResponse.getBytes());
                out.write(imageByArray);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String getHTMLResponse() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Simple Form</title>
            </head>
            <body>
                <h1>Welcome to my server</h1>
                
                <form action="/" method="POST">
                    <label for="inputText">Enter code like 201 and get image:</label>
                    <input type="text" id="inputText" name="inputText">
                    <button type="submit">Submit
                </form>
            </body>
            </html>
            """;
    }

    private static void runClientOnServer(String host, int port) {
        pool.submit(() -> {
            try (Socket socket = new Socket(host, port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 OutputStream out = socket.getOutputStream()) {

                System.out.println("Подключено клиентом к серверу: http://" + host + ":" + port);

                String messageToSend = "Java" + END_OF_MESSAGE_MARK;
                out.write(messageToSend.getBytes());
                out.flush();

                String response = in.readLine();
                System.out.println("Ответ от сервера: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
