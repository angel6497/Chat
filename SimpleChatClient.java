// The class allows the transmission of text messages making use of the server class

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class SimpleChatClient{

  JTextField outgoing;
  JTextArea incoming;
  BufferedReader reader;
  PrintWriter writer;
  Socket sock;

  public static void main(String[] args){
    SimpleChatClient scc = new SimpleChatClient();
    scc.setUpGUI();
    scc.setUpNetWorking();
  }

// This method simply sets up the GUI where messages can be sent and received
  private void setUpGUI(){
    JFrame frame = new JFrame("Simple Chat Client");
    JPanel panel = new JPanel();
    outgoing = new JTextField(21);
    incoming = new JTextArea(7, 27);
    JScrollPane scroller = new JScrollPane(incoming);
    JButton send = new JButton("Send");

    incoming.setLineWrap(true);
    incoming.setEditable(false);
    incoming.setWrapStyleWord(true);

    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    send.addActionListener(new sendListener());

    panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    panel.add(scroller);
    panel.add(outgoing);
    panel.add(send);

    frame.getContentPane().add(BorderLayout.CENTER, panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400, 210);
    frame.setVisible(true);

    outgoing.requestFocus();

  }

  // This method sets up the network with the server
  private void setUpNetWorking(){
    try{
      // The client will look for a server on port 5000
      // Right now the network will use the local host
      sock = new Socket("localhost", 5000);
      writer = new PrintWriter(sock.getOutputStream());
      InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
      reader = new BufferedReader(streamReader);
      // A new thread is created which every new client
      Thread readerThread = new Thread(new IncomingReader());
      readerThread.start();
      System.out.println("Network Stablished.");
    }
    catch(Exception ex){
      System.out.println("Network Set Up Error...");
      ex.printStackTrace();
    }
  }

  // Runnable class for the client thread with reads from the server's socket stream
  public class IncomingReader implements Runnable{
    public void run(){
      try{
        String message;
        while((message = reader.readLine()) != null){
          System.out.println("Read " + message);
          incoming.append(message + "\n");
        }
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }

  // Listener for the send button, that sends whats in the text field to the server
  public class sendListener implements ActionListener{
    public void actionPerformed(ActionEvent ev){
      String message = outgoing.getText();
      try{
        writer.println(message);
        writer.flush();
      }
      catch(Exception ex){
        System.out.println("Send Error...");
        ex.printStackTrace();
      }
      outgoing.setText("");
      outgoing.requestFocus();
    }
  }

}
