/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author admin
 */
public class ClientHandler implements Runnable{

private static ArrayList<String> names = new ArrayList<>();
    private static ArrayList<PrintWriter> writers = new ArrayList<>();
    private String name;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Lock lock = new ReentrantLock();

    public ClientHandler(Socket socket) {
        this.socket=socket;}       
    @Override
    public void run() { 
        String user = "";
        try{
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter a Username");
            name = in.readLine();
            lock.lock();
            while (names.contains(name) || name.isEmpty()) {
                out.println("Username already taken. Try new Username");
                name = in.readLine();
            }
            names.add(name);
            lock.unlock();
            out.println("Username is accepted. Welcome to the chat! ");

            toAll("SERVER", name + "  joined the chat");
            writers.add(out);
            user = name;
            while (true) {
                String msg = in.readLine();
                if (msg.equals("LOGOUT") || msg.equals("logout")|| 
                        msg.equals("SIGNOUT")||msg.equals("signout")) {
                    return;
                }
                toAll(user, msg);
            }
        } catch (IOException e) {
            System.err.println("Connecton with " + user + " end abruptly");


        } finally {
            lock.lock();
            writers.remove(out);
            names.remove(name);
            lock.unlock();
            toAll("SERVER", user + " left the chat");
            System.out.println(user + " left the chat");

            try {
                socket.close();
                in.close();
            } catch (IOException e) {
                System.err.println("Error closing the BufferedReader");
            }
            out.close();
        }
    }

        

    private  void toAll(String user, String message) {
        
    for(PrintWriter writer:writers){
        writer.println("--> " +user+": "+message);

        }
    }
}
