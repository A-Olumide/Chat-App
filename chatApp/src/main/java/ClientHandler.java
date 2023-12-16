import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {


    // keep track of all clients, so we can loop through and send messages to multiple clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsername;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage(clientUsername + " has entered the chat!");
        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);

        }
    }



    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient=="/quit"){

                }
                broadcastMessage(messageFromClient);
            } catch (Exception e) {
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    public void broadcastMessage(String messageToSend) {
        for(ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                   // if (messageToSend != null) {
                        clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                //}
            }
            } catch (IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    public void removeClientHandler() {

        broadcastMessage(clientUsername + " has left the chat!");
        clientHandlers.remove(this);

    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
            e.printStackTrace();
        }
    }
}
