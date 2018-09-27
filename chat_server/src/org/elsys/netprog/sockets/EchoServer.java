package org.elsys.netprog.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		Manager manager = new Manager();

		try {
            serverSocket = new ServerSocket(10001);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("client connected from " + clientSocket.getInetAddress());

                ClientHandler client = new ClientHandler(clientSocket, manager);
                manager.addClient(client);
                client.start();
            }
//		} catch (Throwable t) {
//			System.out.println(t.getMessage());
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			
			System.out.println("Server closed");
		}
	}

}
