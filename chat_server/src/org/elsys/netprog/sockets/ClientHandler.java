package org.elsys.netprog.sockets;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private Manager manager;

    public ClientHandler(Socket socket, Manager manager) throws IOException {
        reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
         writer = new PrintWriter(socket.getOutputStream(), true);
        this.manager = manager;
    }

    public void send(String msg) {
        writer.println(msg);
    }

    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                if (msg != null)
                    manager.broadcast(msg, this);
                else
                    break;
            }
        } catch (IOException e) {

        }
        manager.notifyDead(this);
        System.out.println("Client closed connection");
    }
}
