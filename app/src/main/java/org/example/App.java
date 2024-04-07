/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
            server.createContext("/test", new MyHandler());
            server.createContext("/upload", new UploadingFileEndpoint());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;
            if (Objects.equals(t.getRequestMethod(), "GET")) {
                response = "This is the response";
                t.sendResponseHeaders(200, response.length());
            } else {
                response = "There is no such request.";
                t.sendResponseHeaders(404, response.length());
            }

            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class UploadingFileEndpoint implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStream inputStream = t.getRequestBody();
            ObjectMapper objectMapper = new ObjectMapper();
        
            try {
                File file = objectMapper.readValue(inputStream, File.class);
                Storage.Store(file, "./storage", "random-text.pdf");

                // Use the person object as needed
                // create a UploadFileResponse object
                UploadFileResponse uploadFileResponse = new UploadFileResponse("File uploaded successfully with name: " + file.fileName);
                String response = objectMapper.writeValueAsString(uploadFileResponse);
                
                t.getResponseHeaders().set("Content-Type", "application/json");
                t.sendResponseHeaders(200, response.length());
                
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class File {
        public String fileName;
        public String directory;

        public File() {}
    }

    static class UploadFileResponse {
        public String message;

        public UploadFileResponse() {}

        public UploadFileResponse(String message) {
            this.message = message;
        }
    }

    static class Storage {
        public static List<File> files = new ArrayList<File>();

        public Storage() {}

        public static void Store(File file, String directory, String fileName) {
            // save the file into directory

            // save the file into the database
            files.add(file);
        }
    }
}
