import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner input = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = input.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }
    public void waitForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
              String messageFromGroupChat;

              while (socket.isConnected()) {
                  try{
                      messageFromGroupChat = bufferedReader.readLine();
                      System.out.println(messageFromGroupChat);
                  } catch (Exception e) {
                      closeAll(socket, bufferedReader, bufferedWriter);
                  }
              }
            }
        }).start();
    }
   public  void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
       try{
           if(bufferedReader != null) {
               bufferedReader.close();

           }
           if(bufferedWriter != null) {
               bufferedWriter.close();
           }
           if(socket != null) {
               socket.close();
           }
       } catch (Exception e) {
           //System.out.println(e.getMessage());
           e.printStackTrace();
       }
   }

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter username for the group chat: ");
        String username = input.nextLine();
        Socket socket = new Socket("localhost", 9112);
        Client client = new Client(socket, username);
        client.waitForMessage();
        client.sendMessage();
    }
}
