/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;


/**
 *
 * @author admin
 */
public class Server {

    private static final int PORT=8888;
    /**
     * @param args the command line arguments
     */
    
    
    
    private static ArrayList<String> names = new ArrayList<>();
    private static ArrayList<PrintWriter> writers = new ArrayList<>();
     
    public static void main(String[] args) throws IOException {
        
        ServerSocket listener=new ServerSocket(PORT);
        while(true){
        System.out.println("[SERVER] Waiting for client connection...");
        Thread client = new Thread(new ClientHandler(listener.accept()));

                client.start();
                System.out.println("[SERVER] Connected to client!");
        
        System.out.println("[SERVER] Connected to client!");
        
       
   
        
        }

    }


    
    
}
