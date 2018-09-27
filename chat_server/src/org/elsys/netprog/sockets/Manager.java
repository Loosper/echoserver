package org.elsys.netprog.sockets;

import java.util.ArrayList;

public class Manager {
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public void broadcast(String msg, ClientHandler origin) {
        System.out.println("Broadcasting message: " + msg);

        synchronized (clients) {
            for (ClientHandler client: clients) {
                if (client != origin)
                    client.send(msg);
            }
        }
    }

    public void printClients() {
        System.out.println("Current clients: " + Integer.toString(clients.size()));
    }

    public void addClient(ClientHandler client) {
        synchronized (clients) {
            clients.add(client);
            printClients();
        }
    }

    public void notifyDead(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
            printClients();
        }

        System.out.println("Client out");
    }
}
