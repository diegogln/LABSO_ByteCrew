package client;


import java.io.IOException;
import java.net.Socket;

public class Client {
   public static void main(String[] args) {
	   
	   if (args.length < 2) {
           System.err.println("Usage: java Client <host> <port>");
           return;
       }

       String host = args[0];
       int port = Integer.parseInt(args[1]);

       try {
           Socket socket = new Socket(host, port);
           System.out.println("Connected to server");

           System.out.println("Inserisci la modalita' in cui vuoi operare e il relativo topic");

           /*
            * Delega la gestione di input/output a due thread separati, uno per inviare
            * messaggi e uno per leggerli
            */
           Thread sender = new Thread(new Sender(socket));
           Thread receiver = new Thread(new Receiver(socket, sender));

           sender.start();
           receiver.start();

           try {
               /* rimane in attesa che sender e receiver terminino la loro esecuzione */
               sender.join();
               receiver.join();
               socket.close();
               System.out.println("Socket closed.");
           } catch (InterruptedException e) {
               /*
                * se qualcuno interrompe questo thread nel frattempo, terminiamo
                */
               return;
           }

       } catch (IOException e) {
           System.err.println("IOException caught: " + e);
           e.printStackTrace();
       }
   }
}