// Server class that receives messages from multiple clients and distributes them to all of them

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleChatServer{

  ArrayList clientOutputStreams;
  int i = 1;

  // Inner class executed by the server thread
  public class ClientHandler implements Runnable{
    BufferedReader reader;
    Socket sock;

    // The constructor creates a ClientHandler that's specific for a certain client
    public ClientHandler(Socket clientSocket){
      try{
        sock = clientSocket;
        InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
        reader = new BufferedReader(streamReader);
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
    }

    // The run method reads incomming messages from a client and sends them to everyone else
    public void run(){
      String message;
      try{
        while((message = reader.readLine()) != null){
          String name = Thread.currentThread().getName();
          System.out.println("Read " + name + ": " + message);
          tellEveryone(name + ": " + message);
        }
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }

  public static void main(String[] args){
    SimpleChatServer scs = new SimpleChatServer();
    scs.go();
  }

  // This method sets up the server on port 5000
  public void go(){
    clientOutputStreams = new ArrayList();
    try{
      ServerSocket serverSock = new ServerSocket(5000);
      System.out.println("Network Stablished." + "IP: " + InetAddress.getLocalHost());

      // The server creates a new ClientHandler for every new clients that makes a connection
      while(true){
        Socket clientSock = serverSock.accept();
        PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
        clientOutputStreams.add(writer);

        Thread t = new Thread(new ClientHandler(clientSock));
        // A name is given to every thread to be able to know who is sending every message
        t.setName("Client " + i);
        i++;
        t.start();
        System.out.println("Got a Connection.");
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
  }

  // This method sends a message to every client connected so far
  public void tellEveryone(String message){
    Iterator it = clientOutputStreams.iterator();
    while(it.hasNext()){
      try{
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(message);
        writer.flush();
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }

}
