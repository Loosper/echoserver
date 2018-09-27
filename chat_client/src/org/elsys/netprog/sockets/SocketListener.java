package org.elsys.netprog.sockets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketListener extends Thread {
    private BufferedReader reader;

    public SocketListener(Socket socket) throws IOException{
        reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
    }

    public void run() {
        try {
            while (true) {
                String msg = reader.readLine();
                if (msg != null) {
                    System.out.println("Server says: " + msg);
                } else {
                    System.out.println("Server closed connection");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Server closed connection");
        }

        System.exit(0);
    }
}
