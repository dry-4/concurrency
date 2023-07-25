package com.example.concurrency.multi_threaded_tcp_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTcpServer {

    private static void goDo(Socket socket) throws IOException, InterruptedException {
        byte[] request = new byte[1024];

        // Read the HTTP request
        InputStream inputStream = socket.getInputStream();

        // Mimicking Long-running Job
        System.out.println("Processing Request");
        Thread.sleep(5000);

        // Returning the response and closing
        System.out.println("processing complete...");
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("HTTP/1.1 200 OK\r\n\r\nHello, World!\r\n".getBytes());
        outputStream.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(1907);
        System.out.println("Listening on :1907");

        while (true) {
            Socket socket = serverSocket.accept();

            System.out.println("new client connected");

            goDo(socket);
        }
    }
}
